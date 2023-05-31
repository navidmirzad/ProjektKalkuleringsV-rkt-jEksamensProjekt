package com.example.projektkalkuleringsprojekt2semexam.repository;

import com.example.projektkalkuleringsprojekt2semexam.model.Project;
import com.example.projektkalkuleringsprojekt2semexam.model.Subproject;
import com.example.projektkalkuleringsprojekt2semexam.model.*;
import com.example.projektkalkuleringsprojekt2semexam.repository.util.DatabaseCon;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository("mysqldb-project")
public class ProjectRepository implements IProjectRepository {

    // Project #1

    @Override
    public void createProject(Project project, List<Integer> listOfUsers) {

        try {
            Connection con = DatabaseCon.getConnection();
            con.setAutoCommit(false);

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

            con.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Project> getProjectsByUserId(int id) {
        List<Project> projects = new ArrayList<>();

        try {
            Connection con = DatabaseCon.getConnection();

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

    @Override
    public List<Project> getProjects() {

        List<Project> projects = new ArrayList<>();

        try {
            Connection con = DatabaseCon.getConnection();

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

    @Override
    public int estimatedTimeForProject(int projectid) {
        int totalEstimatedTime = 0;
        List<Subproject> subprojects = getSubprojectByProjectId(projectid);
        for (Subproject subproject : subprojects) {
            totalEstimatedTime += subproject.getTotalEstimatedTime();
        }
        return totalEstimatedTime;
    }

    @Override
    public Project findProjectByID(int id) {

        Project project = null;

        try {
            Connection con = DatabaseCon.getConnection();
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

    @Override
    public int getProjectIdBySubprojectId(int subprojectId) {
        int projectId = 0;

        try {
            Connection con = DatabaseCon.getConnection();
            String getSubprojectIdQuery = "SELECT projectId FROM subproject WHERE subprojectId = ?";
            PreparedStatement preparedStatement = con.prepareStatement(getSubprojectIdQuery);
            preparedStatement.setInt(1, subprojectId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                projectId = resultSet.getInt("projectid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projectId;
    }

    @Override
    public void editProject(int id, Project editedProject, List<Integer> listOfUsers) {

        try {
            Connection con = DatabaseCon.getConnection();
            con.setAutoCommit(false);

            String deleteExistingRows = "DELETE FROM users_projects WHERE projectid = ?";
            PreparedStatement deleteStatement = con.prepareStatement(deleteExistingRows);
            deleteStatement.setInt(1, id);
            deleteStatement.executeUpdate();

            for (Integer userid : listOfUsers) {
                String updateUsersSubprojects = "INSERT INTO users_projects (userid, projectid) VALUES (?,?)";
                PreparedStatement preparedStatement = con.prepareStatement(updateUsersSubprojects);
                preparedStatement.setInt(1, userid);
                preparedStatement.setInt(2, id);
                preparedStatement.executeUpdate();
            }

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

            con.commit();

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Update failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProject(int id) {

        try {

            Connection con = DatabaseCon.getConnection();
            con.setAutoCommit(false);

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

            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Account section

    @Override
    public List<User> getUsers() {

        List<User> users = new ArrayList<>();

        try {

            Connection con = DatabaseCon.getConnection();

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

    @Override
    public List<User> getUsersByProjectId(int projectId) {

        List<User> users = new ArrayList<>();

        try {
            Connection con = DatabaseCon.getConnection();

            String sql = "SELECT * FROM user INNER JOIN users_projects ON user.userid = users_projects.userid WHERE projectid = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, projectId);
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

    @Override
    public List<User> getUsersBySubprojectId(int subprojectId) {

        List<User> users = new ArrayList<>();

        try {

            Connection con = DatabaseCon.getConnection();

            String sql = "SELECT * FROM user INNER JOIN users_subprojects ON user.userid = users_subprojects.userid WHERE subprojectid = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, subprojectId);
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

    @Override
    public void createSubproject(List<Integer> listOfUsers, int projectid, Subproject subproject) {

        try {

            Connection con = DatabaseCon.getConnection();
            con.setAutoCommit(false);

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

            con.commit();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getEstimatedTimeForSubproject(int subprojectid) {

        int estimatedTime = 0;

        try {

            Connection con = DatabaseCon.getConnection();

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

    @Override
    public List<Subproject> getSubprojectByProjectId(int projectid) {

        List<Subproject> subprojects = new ArrayList<>();

        try {

            Connection con = DatabaseCon.getConnection();

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

    @Override
    public Subproject getSubprojectById(int id) {

        Subproject subproject = new Subproject();

        try {

            Connection con = DatabaseCon.getConnection();

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

    @Override
    public int getSubprojectIdByTaskId(int taskId) {
        int subprojectId = 0;

        try {

            Connection con = DatabaseCon.getConnection();

            String getSubprojectIdQuery = "SELECT subprojectID FROM task WHERE taskID = ?";
            PreparedStatement preparedStatement = con.prepareStatement(getSubprojectIdQuery);
            preparedStatement.setInt(1, taskId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                subprojectId = resultSet.getInt("subprojectID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subprojectId;
    }

    @Override
    public void editSubproject(int id, Subproject editedSubproject, List<Integer> listOfUsers) {

        try {

            Connection con = DatabaseCon.getConnection();
            con.setAutoCommit(false);

            String deleteExistingRows = "DELETE FROM users_subprojects WHERE subprojectid = ?";
            PreparedStatement deleteStatement = con.prepareStatement(deleteExistingRows);
            deleteStatement.setInt(1, id);
            deleteStatement.executeUpdate();

            for (Integer userid : listOfUsers) {
                String updateUsersSubprojects = "INSERT INTO users_subprojects (userid, subprojectid) VALUES (?,?)";
                PreparedStatement preparedStatement = con.prepareStatement(updateUsersSubprojects);
                preparedStatement.setInt(1, userid);
                preparedStatement.setInt(2, id);
                preparedStatement.executeUpdate();
            }

            String sql = "UPDATE subproject SET subprojectName = ?, description = ?, estimatedTime = ? WHERE subprojectID = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, editedSubproject.getProjectName());
            preparedStatement.setString(2, editedSubproject.getDescription());
            preparedStatement.setInt(3, editedSubproject.getEstimatedTime());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();

            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteSubproject(int id) {

        try {

            Connection con = DatabaseCon.getConnection();
            con.setAutoCommit(false);

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

            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // TASK SECTION

    @Override
    public void createTask(List<Integer> listOfUsers, int subprojectid, Task task) {

        //insert task table

        try {

            Connection con = DatabaseCon.getConnection();
            con.setAutoCommit(false);

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

            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteTask(int taskid) {

        try {

            Connection con = DatabaseCon.getConnection();
            con.setAutoCommit(false);

            String sqlJoin = "DELETE FROM users_tasks WHERE taskid = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sqlJoin);
            preparedStatement.setInt(1,taskid);
            preparedStatement.executeUpdate();

            String sqlTable = "DELETE FROM task WHERE taskid = ?";
            PreparedStatement preparedStatement1 = con.prepareStatement(sqlTable);
            preparedStatement1.setInt(1,taskid);
            preparedStatement1.executeUpdate();

            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Task getTaskById(int taskId) {

        try {

            Connection con = DatabaseCon.getConnection();

            String sql = "SELECT * FROM task WHERE taskid = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1,taskId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Task(resultSet.getInt("taskid"),
                        resultSet.getString("taskName"),
                        resultSet.getString("description"),
                        resultSet.getInt("estimatedTime"),
                        resultSet.getInt("subprojectid"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void editTask(int taskId, Task editedTask, List<Integer> listOfUsers) {

        try {

            Connection con = DatabaseCon.getConnection();
            con.setAutoCommit(false);

            String deleteExistingRows = "DELETE FROM users_tasks WHERE taskid = ?";
            PreparedStatement deleteStatement = con.prepareStatement(deleteExistingRows);
            deleteStatement.setInt(1, taskId);
            deleteStatement.executeUpdate();

            for (Integer userid : listOfUsers) {
                String updateUsersSubprojects = "INSERT INTO users_tasks (userid, taskid) VALUES (?,?)";
                PreparedStatement preparedStatement = con.prepareStatement(updateUsersSubprojects);
                preparedStatement.setInt(1, userid);
                preparedStatement.setInt(2, taskId);
                preparedStatement.executeUpdate();
            }

            String updateTask = "UPDATE task SET taskname = ?, description = ?, estimatedtime = ? WHERE taskID = ?";
            PreparedStatement preparedStatement1 = con.prepareStatement(updateTask);
            preparedStatement1.setString(1, editedTask.getProjectName());
            preparedStatement1.setString(2, editedTask.getDescription());
            preparedStatement1.setInt(3, editedTask.getEstimatedTime());
            preparedStatement1.setInt(4, taskId);
            preparedStatement1.executeUpdate();

            con.commit();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }




}