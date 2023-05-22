package com.example.projektkalkuleringsprojekt2semexam.service;

import com.example.projektkalkuleringsprojekt2semexam.model.Project;
import com.example.projektkalkuleringsprojekt2semexam.model.User;
import com.example.projektkalkuleringsprojekt2semexam.repository.MainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // Account

    public void createUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getUserPassword());
        user.setUserPassword(encodedPassword);
        mainRepository.createUser(user);
    }

    public User getUser(String uid) {
        return mainRepository.getUser(uid);
    }

    public User getUserById(int id) {
        return mainRepository.getUserById(id);
    }

    public void editAccount(int id, User editedUser) {
        String encodedPassword = passwordEncoder.encode(editedUser.getUserPassword());
        editedUser.setUserPassword(encodedPassword);
        mainRepository.editAccount(id, editedUser);
    }

    public void deleteAccount(int userID) {
        mainRepository.deleteAccount(userID);
    }

    public User getUserByUserNameAndPassword(String userName, String password) {
        User user = mainRepository.getUser(userName);

        if (user != null) {
            String encodedPassword = user.getUserPassword();
            boolean passwordMatch = passwordEncoder.matches(password, encodedPassword);
            if (passwordMatch) {
                return user;
            }
        }
        return null;
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
        mainRepository.createSubproject(userid,projectid,subproject);
    }

    public List<Subproject> getSubprojectByProjectId(int projectid) {
        return mainRepository.getSubprojectByProjectId(projectid);
    }

    public Subproject getSubprojectById(int id) {
        return mainRepository.getSubprojectById(id);
    }

    public void editSubproject(int id, Subproject editedSubproject) {
        mainRepository.editSubproject(id, editedSubproject);
    }

    public void deleteSubproject(int id) {
        mainRepository.deleteSubproject(id);
    }


}
