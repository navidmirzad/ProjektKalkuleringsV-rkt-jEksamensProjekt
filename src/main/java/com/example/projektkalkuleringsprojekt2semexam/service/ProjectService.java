package com.example.projektkalkuleringsprojekt2semexam.service;

import com.example.projektkalkuleringsprojekt2semexam.model.Project;
import com.example.projektkalkuleringsprojekt2semexam.model.Subproject;
import com.example.projektkalkuleringsprojekt2semexam.model.User;
import com.example.projektkalkuleringsprojekt2semexam.repository.IProjectRepository;
import com.example.projektkalkuleringsprojekt2semexam.model.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private IProjectRepository projectRepository;

    public ProjectService(ApplicationContext context, @Value("${project.repository.impl}") String impl) {
        projectRepository = (IProjectRepository) context.getBean(impl);
    }

    // Project


    public List<User> getUsers() {
        return projectRepository.getUsers();
    }

    public void createProject(Project project, List<Integer> listOfUsers) {
        projectRepository.createProject(project, listOfUsers);
    }

    public List<Project> getProject() {
        return projectRepository.getProjects();
    }

    public List<Project> getProjectsByUserId(int id) {
        return projectRepository.getProjectsByUserId(id);
    }

    public Project findProjectByID(int id) {
        return projectRepository.findProjectByID(id);
    }

    public void editProject(int id, Project editedProject, List<Integer> listOfUsers) {
        projectRepository.editProject(id, editedProject, listOfUsers);
    }

    public void deleteProject(int id) {
        projectRepository.deleteProject(id);
    }

    public List<User> getUsersByProjectId(int projectId) {
        return projectRepository.getUsersByProjectId(projectId);
    }

    public List<User> getUsersBySubpojectId(int subprojectId) {
        return projectRepository.getUsersBySubprojectId(subprojectId);
    }

    public int getProjectIdBySubprojectId(int subprojectId) {
        return projectRepository.getProjectIdBySubprojectId(subprojectId);
    }


    //SUBPROJECTS

    public void createSubproject(List<Integer> listOfUsers, int projectid, Subproject subproject) {
        projectRepository.createSubproject(listOfUsers,projectid,subproject);
    }

    public List<Subproject> getSubprojectByProjectId(int projectid) {
        return projectRepository.getSubprojectByProjectId(projectid);
    }

    public Subproject getSubprojectById(int id) {
        return projectRepository.getSubprojectById(id);
    }

    public void editSubproject(int id, Subproject editedSubproject, List<Integer> listOfUsers) {
        projectRepository.editSubproject(id, editedSubproject, listOfUsers);
    }

    public void deleteSubproject(int id) {
        projectRepository.deleteSubproject(id);
    }

    public int getSubprojectIdByTaskId(int taskId) {
        return projectRepository.getSubprojectIdByTaskId(taskId);
    }

    //TASKS

    public void createTask(List<Integer> listOfUsers, int subprojectid, Task task) {
        projectRepository.createTask(listOfUsers,subprojectid,task);
    }

    public void deleteTask(int taskid) {
        projectRepository.deleteTask(taskid);
    }

    public Task getTaskById(int taskId) {
        return projectRepository.getTaskById(taskId);
    }

    public void editTask(int taskId, Task editedTask, List<Integer> listOfUsers) {
        projectRepository.editTask(taskId, editedTask, listOfUsers);
    }


}
