package com.example.projektkalkuleringsprojekt2semexam.repository;

import com.example.projektkalkuleringsprojekt2semexam.model.Project;
import com.example.projektkalkuleringsprojekt2semexam.model.Role;
import com.example.projektkalkuleringsprojekt2semexam.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MainRepository {

    @Value("${spring.datasource.url}")
    private String db_url;

    @Value("${spring.datasource.username}")
    private String uid;

    @Value("${spring.datasource.password}")
    private String pwd;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(db_url, uid, pwd);
    }

    public List<Project> getProjects(int id) {
        List<Project> projects = new ArrayList<>();

        try (Connection con = getConnection()) {


            String sql = "SELECT project.projectID, project.name, project.description, \n" +
                    "project.estimatedTime, project.startDate, project.endDate, project.projectRank, \n" +
                    "project.isDone, users_projects.userid from project join users_projects on project.projectid=users_projects.userid where userID = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                projects.add(new Project(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getInt(4), resultSet.getString(5), resultSet.getString(6), resultSet.getInt(7),
                        resultSet.getBoolean(8)));
            }

            return projects;
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createProject(int id, Project project) {

        try (Connection con = getConnection()) {

            String insertList = "INSERT INTO project (project.name, project.description, +\n" +
                    "\"project.estimatedTime, project.startDate, project.endDate, project.projectRank, users_projects.userid\") VALUES(?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = con.prepareStatement(insertList);
            preparedStatement.setString(1, project.getName());
            preparedStatement.setString(2, project.getDescription());
            preparedStatement.setInt(3, project.getEstimatedTime());
            preparedStatement.setString(4, project.getStartDate());
            preparedStatement.setString(5, project.getEndDate());
            preparedStatement.setInt(6, project.getProjectRank());
            preparedStatement.setInt(7, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editProject(int listid, Project editedProject) {

        try (Connection con = getConnection()) {

            //find wishlist and set it to editedWishlist
            String sql = "UPDATE project SET project.name = ?, project.description =?, project.estimatedTime = ?, " +
                    "project.startDate = ?, project.endDate = ?, project.projectRank = ?, project.isDone = ? WHERE projectid = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, editedProject.getName());
            preparedStatement.setString(2, editedProject.getDescription());
            preparedStatement.setInt(3, editedProject.getEstimatedTime());
            preparedStatement.setString(4, editedProject.getStartDate());
            preparedStatement.setString(5, editedProject.getEndDate());
            preparedStatement.setInt(6, editedProject.getProjectRank());
            preparedStatement.setBoolean(7, editedProject.isDone());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProject(int id) {

        try (Connection conn = getConnection()) {

            try (PreparedStatement pstmt2 = conn.prepareStatement("DELETE FROM project WHERE projectid = ?")) {
                pstmt2.setInt(1, id);
                pstmt2.execute();
            }
        } catch (SQLException e) {

        }
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
            String SQL = "SELECT userid, userName, userPassword from users where userName = ?";
            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {
                int userID = resultSet.getInt("userID");
                String userName1 = resultSet.getString("userName");
                String userPassword1 = resultSet.getString("userPassword");
                if (userName1.equals(userName)) {
                    return new User(userID,userName1,userPassword1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public User getUserById(int id) {

        try (Connection con = getConnection()) {
            String sql = "SELECT * FROM users WHERE userid = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            String enumValue;

            if (resultSet.next()) {

                enumValue = resultSet.getString("role");

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

    public void deleteAccount(int id) {

        try (Connection con = getConnection()){

            String sqlWishes = "DELETE FROM wishes WHERE listid IN (SELECT listid FROM wish_lists WHERE userid = ?)";
            PreparedStatement psWishes = con.prepareStatement(sqlWishes);
            psWishes.setInt(1, id);
            psWishes.executeUpdate();

            String sqlWishlist = "DELETE FROM wish_lists WHERE userid = ?";
            PreparedStatement preparedStatementList = con.prepareStatement(sqlWishlist);
            preparedStatementList.setInt(1, id);
            preparedStatementList.execute();

            String sqlUser = "DELETE FROM users WHERE userid = ?";
            PreparedStatement preparedStatementUser = con.prepareStatement(sqlUser);
            preparedStatementUser.setInt(1,id);
            preparedStatementUser.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void editAccount(int id, User editedUser) {

        try (Connection con = getConnection()) {

            //find wish and set it to editedWish
            String sql = "UPDATE users SET userName = ?, userPassword = ?, firstName = ?, lastName = ?, birthDate = ?, gender = ?, email = ?, phoneNumber = ? WHERE userid = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, editedUser.getUserName());
            preparedStatement.setString(2, editedUser.getUserPassword());
            preparedStatement.setString(3, editedUser.getFirstName());
            preparedStatement.setString(4, editedUser.getLastName());
            preparedStatement.setString(5, editedUser.getBirthDate());
            preparedStatement.setString(6, String.valueOf(editedUser.getRole()));
            preparedStatement.setString(7, editedUser.getEmail());
            preparedStatement.setInt(8, editedUser.getPhoneNumber());
            preparedStatement.setInt(9, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}