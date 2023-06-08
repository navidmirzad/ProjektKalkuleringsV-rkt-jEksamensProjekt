package com.example.projektkalkuleringsprojekt2semexam.service;

import com.example.projektkalkuleringsprojekt2semexam.model.User;
import com.example.projektkalkuleringsprojekt2semexam.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository ) {
        this.accountRepository = accountRepository;
    }

    // Account

    public void createUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getUserPassword()); // opretter en String kaldt encodedPassword, som kalder interface klassen passwordEncoder
        user.setUserPassword(encodedPassword);  // og encoder så user.getUserPassword. // Herefter sætter vi så brugerens (user) kodeord til at være det encodedePassword
        accountRepository.createUser(user); // kalder til repository, og opretter brugeren og giver den med i paramteren.
    }

    public User getUserById(int id) {
        return accountRepository.getUserById(id);
    }

    public void editAccount(int id, User editedUser) {
        String encodedPassword = passwordEncoder.encode(editedUser.getUserPassword());
        editedUser.setUserPassword(encodedPassword);
        accountRepository.editAccount(id, editedUser);
    }

    public void deleteAccount(int id) {
        accountRepository.deleteAccount(id);
    }

    public User getUserByUserNameAndPassword(String userName, String password) {
        User user = accountRepository.getUser(userName);

        if (user != null) {
            String encodedPassword = user.getUserPassword();
            boolean passwordMatch = passwordEncoder.matches(password, encodedPassword);
            if (passwordMatch) {
                return user;
            }
        }
        return null;
    }

}
