package com.example.projektkalkuleringsprojekt2semexam.service;

import com.example.projektkalkuleringsprojekt2semexam.model.Project;
import com.example.projektkalkuleringsprojekt2semexam.model.Subproject;
import com.example.projektkalkuleringsprojekt2semexam.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // Project

    public void createProject(Project project, int userid) {
        projectRepository.createProject(project, userid);
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

    public void createSubproject(int userid, int projectid, Subproject subproject) {
        projectRepository.createSubproject(userid,projectid,subproject);
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


}
