package com.example.projektkalkuleringsprojekt2semexam.repository;

import com.example.projektkalkuleringsprojekt2semexam.model.*;

import java.util.List;

public interface IProjectRepository {

    // Project #1

    void createProject(Project project, List<Integer> listOfUsers);

    List<Project> getProjectsByUserId(int id);

    List<Project> getProjects();

    int estimatedTimeForProject(int projectid);

    Project findProjectByID(int id);

    int getProjectIdBySubprojectId(int subprojectId);

    void editProject(int id, Project editedProject, List<Integer> listOfUsers);

    void deleteProject(int id);

    // Account section

    List<User> getUsers();

    List<User> getUsersByProjectId(int projectId);

    List<User> getUsersBySubprojectId(int subprojectId);

    // Subproject section

    void createSubproject(List<Integer> listOfUsers, int projectid, Subproject subproject);

    int getEstimatedTimeForSubproject(int subprojectid);

    List<Subproject> getSubprojectByProjectId(int projectid);

    Subproject getSubprojectById(int id);

    int getSubprojectIdByTaskId(int taskId);

    void editSubproject(int id, Subproject editedSubproject, List<Integer> listOfUsers);

    void deleteSubproject(int id);

    // TASK SECTION

    void createTask(List<Integer> listOfUsers, int subprojectid, Task task);

    void deleteTask(int taskid);

    Task getTaskById(int taskId);

    void editTask(int taskId, Task editedTask, List<Integer> listOfUsers);



}
