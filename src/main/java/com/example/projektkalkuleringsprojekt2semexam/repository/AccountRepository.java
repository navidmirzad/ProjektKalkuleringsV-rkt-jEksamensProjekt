package com.example.projektkalkuleringsprojekt2semexam.repository;

import com.example.projektkalkuleringsprojekt2semexam.model.Role;
import com.example.projektkalkuleringsprojekt2semexam.model.User;
import com.example.projektkalkuleringsprojekt2semexam.repository.util.DatabaseCon;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository("mysqldb-account")
public class AccountRepository implements IAccountRepository {

    @Override
    public void createUser(User user) {

        try {

            Connection con = DatabaseCon.getConnection();

            String insertUser = "INSERT INTO user(firstName,lastName,userName,userPassword,email,birthDate,phoneNumber,role)\n" +
                    "VALUES(?,?,?,?,?,?,?,?)";

            PreparedStatement preparedStatement = con.prepareStatement(insertUser);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUserName());
            preparedStatement.setString(4, user.getUserPassword());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getBirthDate());
            preparedStatement.setInt(7, user.getPhoneNumber());
            preparedStatement.setString(8, String.valueOf(user.getRole()));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public User getUser(String userName) {


        try {
            Connection con = DatabaseCon.getConnection();

            String SQL = "SELECT userid, userName, userPassword from user where userName = ?";
            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                int userID = resultSet.getInt("userID");
                String userName1 = resultSet.getString("userName");
                String userPassword1 = resultSet.getString("userPassword");
                if (userName1.equals(userName)) {
                    return new User(userID, userName1, userPassword1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public User getUserById(int id) {

        try {

            Connection con = DatabaseCon.getConnection();

            String sql = "SELECT * FROM user WHERE userid = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            String enumValue;

            if (resultSet.next()) {

                enumValue = resultSet.getString("role").toUpperCase();

                return new User(resultSet.getInt("userid"),
                        resultSet.getString("userName"),
                        resultSet.getString("userPassword"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("birthDate"),
                        Role.valueOf(enumValue),
                        resultSet.getString("email"),
                        resultSet.getInt("phoneNumber"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void editAccount(int id, User editedUser) {

        try {

            Connection con = DatabaseCon.getConnection();

            String sql = "UPDATE user SET firstName = ?, lastName = ?, userName = ?, userPassword = ?, email = ?, birthDate = ?, phoneNumber = ?, role = ? WHERE userid = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, editedUser.getFirstName());
            preparedStatement.setString(2, editedUser.getLastName());
            preparedStatement.setString(3, editedUser.getUserName());
            preparedStatement.setString(4, editedUser.getUserPassword());
            preparedStatement.setString(5, editedUser.getEmail());
            preparedStatement.setString(6, editedUser.getBirthDate());
            preparedStatement.setInt(7, editedUser.getPhoneNumber());
            preparedStatement.setString(8, String.valueOf(editedUser.getRole()));
            preparedStatement.setInt(9, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteAccount(int id) {

        try {

            Connection con = DatabaseCon.getConnection();
            con.setAutoCommit(false);

            //users_tasks

            String sqlTasks = "DELETE FROM users_tasks WHERE userid = ?";
            PreparedStatement preparedStatementTasks = con.prepareStatement(sqlTasks);
            preparedStatementTasks.setInt(1,id);
            preparedStatementTasks.execute();

            //users_subprojects

            String sqlSubprojects = "DELETE FROM users_subprojects WHERE userid = ?";
            PreparedStatement preparedStatementSubprojects = con.prepareStatement(sqlSubprojects);
            preparedStatementSubprojects.setInt(1,id);
            preparedStatementSubprojects.execute();

            //users_projects

            String sqlProjects = "DELETE FROM users_projects WHERE userid = ?";
            PreparedStatement preparedStatementProjects = con.prepareStatement(sqlProjects);
            preparedStatementProjects.setInt(1,id);
            preparedStatementProjects.execute();

            //user

            String sqlUser = "DELETE FROM user WHERE userid = ?";
            PreparedStatement preparedStatementUser = con.prepareStatement(sqlUser);
            preparedStatementUser.setInt(1, id);
            preparedStatementUser.execute();

            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
