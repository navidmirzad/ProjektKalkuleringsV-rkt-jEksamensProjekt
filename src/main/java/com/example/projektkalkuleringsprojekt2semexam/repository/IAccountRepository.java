package com.example.projektkalkuleringsprojekt2semexam.repository;
import com.example.projektkalkuleringsprojekt2semexam.model.User;
import org.springframework.stereotype.Repository;

import java.sql.Connection;

public interface IAccountRepository {

    void createUser(User user);

    User getUser(String userName);

    User getUserById(int id);

    void editAccount(int id, User editedUser);

    void deleteAccount(int id);

}
