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

    @Autowired
    private PasswordEncoder passwordEncoder;
    private MainRepository mainRepository;

    // Account

    public ProjectService(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

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

    public void deleteAccount(int id) {
        mainRepository.deleteAccount(id);
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


    // Project (wish)

    public void createProject(Project project) {
        mainRepository.createProject(project);
    }

    public List<Project> getProject(int projectID) {
        return mainRepository.getProjects(projectID);
    }

    public Project findProjectByID(int id) {
        return mainRepository.findProjectByID(id);
    }

    public void editProject(int id, Project editedProject) {
        mainRepository.editProject(id, editedProject);
    }

    public void deleteProject(int id) {
        mainRepository.deleteProject(id);
    }


  /*  public void createProject(int id, Project project) {
        mainRepository.createProject(id, project);
    }

    public Project findProjectByID(int projectID) {
        return mainRepository.findProjectByID(projectID);
    }

    public List<Project> getProjects(int id) {
        return mainRepository.getProjects(id);
    }

    public void editProject(int listid, Project project) {
        mainRepository.editProject(listid, project);
    }

    public void deleteProject(int id) {
        mainRepository.deleteProject(id);
    }*/


}
