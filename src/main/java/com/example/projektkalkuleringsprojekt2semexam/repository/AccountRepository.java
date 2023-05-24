package com.example.projektkalkuleringsprojekt2semexam.repository;

import com.example.projektkalkuleringsprojekt2semexam.model.Role;
import com.example.projektkalkuleringsprojekt2semexam.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class AccountRepository {

    @Value("${spring.datasource.url}")
    private String db_url;

    @Value("${spring.datasource.username}")
    private String uid;

    @Value("${spring.datasource.password}")
    private String pwd;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(db_url, uid, pwd);
    }

    public void createUser(User user) {

        try (Connection con = getConnection()) {

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

    public User getUser(String userName) {
        // User user = new User();

        try (Connection con = getConnection()) {
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

    public User getUserById(int id) {

        try (Connection con = getConnection()) {
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

    public void editAccount(int id, User editedUser) {

        try (Connection con = getConnection()) {

            //find wish and set it to editedWish
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

    public void deleteAccount(int id) {

        try (Connection con = getConnection()) {

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

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
