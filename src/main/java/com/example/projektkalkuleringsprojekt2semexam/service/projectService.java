package com.example.projektkalkuleringsprojekt2semexam.service;

import com.example.projektkalkuleringsprojekt2semexam.model.User;
import com.example.projektkalkuleringsprojekt2semexam.repository.MainRepository;
import org.springframework.stereotype.Service;

@Service
public class projectService {

    private MainRepository mainRepository;

    //  ACCOUNT RELATED SERVICE CALLS

    public void createUser(User user) {
        mainRepository.createUser(user);
    }
    public User getUser(String uid) {
        return mainRepository.getUser(uid);
    }

    public User getUserById(int id) {
        return mainRepository.getUserById(id);
    }

    public void editAccount(int id, User editedUser) {
        mainRepository.editAccount(id, editedUser);
    }

    public void deleteAccount(int id) {
        mainRepository.deleteAccount(id);
    }



}
