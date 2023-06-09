package com.example.projektkalkuleringsprojekt2semexam.service;

import com.example.projektkalkuleringsprojekt2semexam.model.User;
import com.example.projektkalkuleringsprojekt2semexam.repository.IAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired // Autowired er en annotation der tillader at man kan injecte (depency injection) - så slipper man for at lave et nyt objekt og skrive "new"
    private PasswordEncoder passwordEncoder; // er en interface klasse med tre metoder, vi får brug for en af dem senere. Encode
    private IAccountRepository accountRepository;


    public AccountService(ApplicationContext context, @Value("${account.repository.impl}") String impl) { // @Value annotationen bruges til at injecte en en værdi fra acc.repo.impl til impl
        accountRepository = (IAccountRepository) context.getBean(impl); // her kalder vi accountRepo som vi caster til Interface AccountRepo.
        // context.getBean(impl) henter derefter en instans af værdien impl som er vores bean.
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

    public boolean doesUsernameExist(String userName) {
        return accountRepository.doesUsernameExist(userName);
    }

    public void editAccount(int id, User editedUser) {
        String encodedPassword = passwordEncoder.encode(editedUser.getUserPassword()); // opretter en String kaldt encodedPassword, som kalder interface klassen passwordEncoder
        editedUser.setUserPassword(encodedPassword); // og encoder så user.getUserPassword. // Herefter sætter vi så brugeren (editedUser) kodeord til at være det encodedePassword
        accountRepository.editAccount(id, editedUser); // kalder accountRepository.editAccount og giver metoden den brugers userID med,
        // så den ved hvad for en acc der skal ændres og User objektet (editedUser) som indeholder den ændrede data om User objektet.
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
