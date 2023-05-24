package com.example.projektkalkuleringsprojekt2semexam.repository;

import com.example.projektkalkuleringsprojekt2semexam.model.Project;
import com.example.projektkalkuleringsprojekt2semexam.model.Subproject;
import com.example.projektkalkuleringsprojekt2semexam.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectRepository {

    @Value("${spring.datasource.url}")
    private String db_url;

    @Value("${spring.datasource.username}")
    private String uid;

    @Value("${spring.datasource.password}")
    private String pwd;

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(db_url, uid, pwd);
    }


    // Project #1

    public void createProject(Project project, List<Integer> listOfUsers) {

        try (Connection con = getConnection()) {

            String createProject = "insert into project (projectName, description, ImageURL, " +
                                    "estimatedTime, startDate, endDate) "
                                     + "values(?, ?, ?, ?, ?, ?);";

            PreparedStatement pstmt = con.prepareStatement(createProject, Statement.RETURN_GENERATED_KEYS); // return autoincremented keys
            pstmt.setString(1, project.getProjectName());
            pstmt.setString(2, project.getDescription());
            pstmt.setString(3, project.getImageURL());
            pstmt.setInt(4, project.getEstimatedTime());
            pstmt.setDate(5, project.getStartDate());
            pstmt.setDate(6, project.getEndDate());
            pstmt.executeUpdate();

            ResultSet generatedKey = pstmt.getGeneratedKeys();
            int projectid = 0;
            if (generatedKey.next()) {
                projectid = generatedKey.getInt(1);
            }

            String insertSelectedUsers = "INSERT INTO users_projects (userID, projectID) VALUES (?, ?)";
            PreparedStatement selectedUsersStatement = con.prepareStatement(insertSelectedUsers);

            for (Integer userid : listOfUsers) {
                selectedUsersStatement.setInt(1, userid);
                selectedUsersStatement.setInt(2, projectid);
                selectedUsersStatement.executeUpdate();
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Project> getProjectsByUserId(int id) {
        List<Project> projects = new ArrayList<>();

        try (Connection con = getConnection()) {

            String SQL = "SELECT * FROM project INNER JOIN users_projects on project.projectid = users_projects.projectid WHERE users_projects.userid = ?";
            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Project project = new Project();
                project.setProjectID(resultSet.getInt(1));
                project.setProjectName(resultSet.getString(2));
                project.setDescription(resultSet.getString(3));
                project.setImageURL(resultSet.getString(4));
                project.setEstimatedTime(resultSet.getInt(5));
                project.setStartDate(resultSet.getDate(6));
                project.setEndDate(resultSet.getDate(7));
                project.setTotalEstimatedTime(estimatedTimeForProject(project.getProjectID()));
                projects.add(project);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projects;
    }

    public List<Project> getProjects() {

        List<Project> projects = new ArrayList<>();

        try (Connection con = getConnection()) {
            String sql = "SELECT projectID, projectName, description, imageURL," +
                        "estimatedTime, startDate, endDate FROM project";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                projects.add(new Project(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getInt(5),
                        resultSet.getDate(6),
                        resultSet.getDate(7)));


            }
            return projects;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public int estimatedTimeForProject(int projectid) {
        int totalEstimatedTime = 0;
        List<Subproject> subprojects = getSubprojectByProjectId(projectid);
        for (Subproject subproject : subprojects) {
            totalEstimatedTime += subproject.getTotalEstimatedTime();
        }
        return totalEstimatedTime;
    }

    public Project findProjectByID(int id) {

        Project project = null;

        try (Connection con = getConnection()) {
            String sql = "SELECT * FROM project WHERE projectID = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                project = new Project();
                project.setProjectID(resultSet.getInt("projectID"));
                project.setProjectName(resultSet.getString("projectName"));
                project.setDescription(resultSet.getString("description"));
                project.setImageURL(resultSet.getString("ImageURL"));
                project.setEstimatedTime(resultSet.getInt("estimatedTime"));
                project.setStartDate(resultSet.getDate("startDate"));
                project.setEndDate(resultSet.getDate("endDate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return project;
    }

    public void editProject(int id, Project editedProject) {

        try (Connection con = getConnection()) {

            // ID's
            int projectID = 0;

            // find projectID
            String findProjectID = "select projectID from users_projects where projectID = ?;";
            PreparedStatement pstmt = con.prepareStatement(findProjectID);
            pstmt.setInt(1, editedProject.getProjectID());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                projectID = rs.getInt("projectID");
            }

            //find wish and set it to editedWish
            String sql = "UPDATE project SET projectName = ?, description = ?, ImageURL = ?, " +
                            "estimatedTime = ?, startDate = ?, endDate = ? WHERE projectID = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, editedProject.getProjectName());
            preparedStatement.setString(2, editedProject.getDescription());
            preparedStatement.setString(3, editedProject.getImageURL());
            preparedStatement.setInt(4, editedProject.getEstimatedTime());
            preparedStatement.setDate(5, (Date) editedProject.getStartDate());
            preparedStatement.setDate(6, (Date) editedProject.getEndDate());
            preparedStatement.setInt(7, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProject(int id) {

        try (Connection con = getConnection()) {
            try (PreparedStatement pstmt1 = con.prepareStatement("DELETE FROM users_tasks WHERE taskID IN (SELECT taskID FROM task WHERE subprojectID IN (SELECT subprojectID FROM subproject WHERE projectID = ?))")) {
                pstmt1.setInt(1, id);
                pstmt1.executeUpdate();
            }

            try (PreparedStatement pstmt2 = con.prepareStatement("DELETE FROM task WHERE subprojectID IN (SELECT subprojectID FROM subproject WHERE projectID = ?)")) {
                pstmt2.setInt(1, id);
                pstmt2.executeUpdate();
            }

            try (PreparedStatement pstmt3 = con.prepareStatement("DELETE FROM users_subprojects WHERE subprojectID IN (SELECT subprojectID FROM subproject WHERE projectID = ?)")) {
                pstmt3.setInt(1, id);
                pstmt3.executeUpdate();
            }

            try (PreparedStatement pstmt4 = con.prepareStatement("DELETE FROM subproject WHERE projectID = ?")) {
                pstmt4.setInt(1, id);
                pstmt4.executeUpdate();
            }

            try (PreparedStatement pstmt5 = con.prepareStatement("DELETE FROM users_projects WHERE projectID = ?")) {
                pstmt5.setInt(1, id);
                pstmt5.executeUpdate();
            }

            try (PreparedStatement pstmt6 = con.prepareStatement("DELETE FROM project WHERE projectID = ?")) {
                pstmt6.setInt(1, id);
                pstmt6.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Account section


    public List<User> getUsers() {

        List<User> users = new ArrayList<>();

        try (Connection con = getConnection()) {

            String sql = "SELECT * FROM user";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            String enumValue;
            while (resultSet.next()) {
                enumValue = resultSet.getString("role").toUpperCase();
                users.add(new User(resultSet.getInt("userid"),
                        resultSet.getString("userName"),
                        resultSet.getString("userPassword"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("birthDate"),
                        Role.valueOf(enumValue),
                        resultSet.getString("email"),
                        resultSet.getInt("phoneNumber")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }


    // Subproject

    public void createSubproject(List<Integer> listOfUsers, int projectid, Subproject subproject) {

        try (Connection con = getConnection()) {
            String insertSubproject = "INSERT INTO subproject (subprojectname, description, estimatedtime, projectid) VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(insertSubproject, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, subproject.getProjectName());
            preparedStatement.setString(2, subproject.getDescription());
            preparedStatement.setInt(3, subproject.getEstimatedTime());
            preparedStatement.setInt(4, projectid);
            preparedStatement.executeUpdate();

            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            int subprojectid = 0;
            if (generatedKey.next()) {
                subprojectid = generatedKey.getInt(1);
            }

            String insertJoin = "INSERT INTO users_subprojects (userid, subprojectid) VALUES(?,?)";
            PreparedStatement pstm = con.prepareStatement(insertJoin);

            for (Integer userid : listOfUsers) {
                pstm.setInt(1, userid);
                pstm.setInt(2, subprojectid);
                pstm.executeUpdate();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int getEstimatedTimeForSubproject(int subprojectid) {

        int estimatedTime = 0;

        try(Connection con = getConnection()) {

            String sql = "SELECT SUM(task.estimatedTime) AS totalEstimatedTime " +
                    "FROM subproject " +
                    "JOIN task ON subproject.subprojectID = task.subprojectID " +
                    "WHERE subproject.subprojectID = ? " +
                    "GROUP BY subproject.subprojectID, subproject.subprojectName;";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1,subprojectid);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                estimatedTime = resultSet.getInt("totalEstimatedTime");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return estimatedTime;
    }

    public List<Subproject> getSubprojectByProjectId(int projectid) {

        List<Subproject> subprojects = new ArrayList<>();

        try (Connection con = getConnection()){

            String SQL = "SELECT * FROM subproject WHERE projectID = ?";
            PreparedStatement preparedStatement = con.prepareStatement(SQL);
            preparedStatement.setInt(1,projectid);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Subproject subproject = new Subproject();
                subproject.setProjectID(resultSet.getInt("subprojectID"));
                subproject.setProjectName(resultSet.getString("subprojectName"));
                subproject.setDescription(resultSet.getString("description"));
                subproject.setEstimatedTime(resultSet.getInt("estimatedTime"));
                subproject.setTotalEstimatedTime(getEstimatedTimeForSubproject(subproject.getProjectID()));

                String taskSql = "SELECT taskID, taskName, description, estimatedTime FROM task WHERE subprojectID = ?";
                PreparedStatement taskStatement = con.prepareStatement(taskSql);
                taskStatement.setInt(1, subproject.getProjectID());
                ResultSet taskResultSet = taskStatement.executeQuery();

                while (taskResultSet.next()) {
                    Task task = new Task();
                    task.setProjectID(taskResultSet.getInt("taskID"));
                    task.setProjectName(taskResultSet.getString("taskName"));
                    task.setDescription(taskResultSet.getString("description"));
                    task.setEstimatedTime(taskResultSet.getInt("estimatedTime"));

                    subproject.getTasks().add(task);
                }

                subprojects.add(subproject);



            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subprojects;
    }

    public Subproject getSubprojectById(int id) {

        Subproject subproject = new Subproject();

        try (Connection con = getConnection()) {

            String sql = "SELECT * FROM subproject WHERE subprojectid = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                subproject.setProjectID(resultSet.getInt(1));
                subproject.setProjectName(resultSet.getString(2));
                subproject.setDescription(resultSet.getString(3));
                subproject.setEstimatedTime(resultSet.getInt(4));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subproject;
    }

    public void editSubproject(int id, Subproject editedSubproject) {

        try (Connection con = getConnection()) {

            String sql = "UPDATE subproject SET subprojectName = ?, description = ?, estimatedTime = ? WHERE subprojectID = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, editedSubproject.getProjectName());
            preparedStatement.setString(2, editedSubproject.getDescription());
            preparedStatement.setInt(3, editedSubproject.getEstimatedTime());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void deleteSubproject(int id) {

        try (Connection con = getConnection()) {
            try (PreparedStatement pstmt1 = con.prepareStatement("DELETE FROM users_tasks WHERE taskID IN (SELECT taskID FROM task WHERE subprojectID = ?)")) {
                pstmt1.setInt(1, id);
                pstmt1.executeUpdate();
            }

            try (PreparedStatement pstmt2 = con.prepareStatement("DELETE FROM task WHERE subprojectID = ?")) {
                pstmt2.setInt(1, id);
                pstmt2.executeUpdate();
            }

            try (PreparedStatement pstmt3 = con.prepareStatement("DELETE FROM users_subprojects WHERE subprojectID = ?")) {
                pstmt3.setInt(1, id);
                pstmt3.executeUpdate();
            }

            try (PreparedStatement pstmt4 = con.prepareStatement("DELETE FROM subproject WHERE subprojectID = ?")) {
                pstmt4.setInt(1, id);
                pstmt4.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // TASK SECTION

    public void createTask(List<Integer> listOfUsers, int subprojectid, Task task) {

        //insert task table

        try (Connection con = getConnection()){
            String insertTask = "INSERT INTO task (taskName, description, estimatedTime, subprojectid)" +
                    "VALUES(?,?,?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(insertTask,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, task.getProjectName());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setInt(3, task.getEstimatedTime());
            preparedStatement.setInt(4, subprojectid);
            preparedStatement.executeUpdate();

            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            int taskid = 0;
            if (generatedKey.next()) {
                taskid = generatedKey.getInt(1);
            }

            String insertJoin = "INSERT INTO users_tasks (userid, taskid) VALUES(?,?)";
            PreparedStatement preparedStatement1 = con.prepareStatement(insertJoin);
            for (Integer userid : listOfUsers) {
                preparedStatement1.setInt(1, userid);
                preparedStatement1.setInt(2, taskid);
                preparedStatement1.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }




}