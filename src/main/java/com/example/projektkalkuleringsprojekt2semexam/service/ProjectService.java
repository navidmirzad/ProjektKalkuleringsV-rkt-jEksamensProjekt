package com.example.projektkalkuleringsprojekt2semexam.service;

import com.example.projektkalkuleringsprojekt2semexam.model.Project;
import com.example.projektkalkuleringsprojekt2semexam.model.Subproject;
import com.example.projektkalkuleringsprojekt2semexam.model.User;
import com.example.projektkalkuleringsprojekt2semexam.repository.ProjectRepository;
import com.example.projektkalkuleringsprojekt2semexam.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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

    public void editProject(int id, Project editedProject) {
        projectRepository.editProject(id, editedProject);
    }

    public void deleteProject(int id) {
        projectRepository.deleteProject(id);
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

    public void editSubproject(int id, Subproject editedSubproject) {
        projectRepository.editSubproject(id, editedSubproject);
    }

    public void deleteSubproject(int id) {
        projectRepository.deleteSubproject(id);
    }

    //TASKS

    public void createTask(List<Integer> listOfUsers, int subprojectid, Task task) {
        projectRepository.createTask(listOfUsers,subprojectid,task);
    }



}
