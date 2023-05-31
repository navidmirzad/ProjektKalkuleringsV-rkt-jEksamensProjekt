package com.example.projektkalkuleringsprojekt2semexam.repository;
import com.example.projektkalkuleringsprojekt2semexam.model.User;

public interface IAccountRepository {

    void createUser(User user);

    User getUser(String userName);

    User getUserById(int id);

    boolean doesUsernameExist(String userName);
    void editAccount(int id, User editedUser);
    void deleteAccount(int id);

}
