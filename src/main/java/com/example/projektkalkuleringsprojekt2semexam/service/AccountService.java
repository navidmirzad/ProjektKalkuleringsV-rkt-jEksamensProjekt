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

    @Autowired // Autowired er en annotation der tillader at man kan injecte (depency injection) - og som automatisk forbinder alle de klasser der skal være bundet
    // i stedet for vi selv gør det, så gør vi brug af IoC og lader spring frameworket tage over den del. @Autowired forbinder dermed PasswordEncoder og IAccountRepository for os.
    private PasswordEncoder passwordEncoder;
    private IAccountRepository accountRepository; // Vi opretter en instans af det repository vi vil bruge


    public AccountService(ApplicationContext context, @Value("${account.repository.impl}") String impl) {
        // @Value gemmer (acc.repo.impl) i String impl, som siger til AccountService hvilken implementation af vores accountRepository vi gerne vil bruge.
        // Constructor injection (ApplicationContext context), bean creation, dependency injection, sørger for AccountService klassen kan interagere med ApplicationContext.
        accountRepository = (IAccountRepository) context.getBean(impl);
        // vi kalder context.getBean(impl) for at få en instans af IAccountRepository bean, fra appContext baseret på den implementation der står i (impl).
        // på den måde kan vi dynamisk ændre den implementation af Repository i AccountService.
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
