package com.example.projektkalkuleringsprojekt2semexam.controller;

import com.example.projektkalkuleringsprojekt2semexam.model.Role;
import com.example.projektkalkuleringsprojekt2semexam.model.User;
import com.example.projektkalkuleringsprojekt2semexam.service.AccountService;
import com.example.projektkalkuleringsprojekt2semexam.service.ProjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

    // opretter accountservice objekt så vi kan få adgang til metoderne i klassen
    private AccountService accountService;

    // opretter en konstruktør så vi kan oprette instanser af accountservice
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Boolean metode der checker på om man er logget ind med en parameteren HttpSession session
    private boolean isLoggedIn(HttpSession session) {
        // session.getAttribute("user") henter den værdi der er gemt i HttpSession objektet under attribute navnet "user".
        // hvis user ikke er null
        return session.getAttribute("user") != null;
    }

    //GetMapping request til logout endpoint, invalidater session også returnerer til index
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // invalidate session and return to login page
        session.invalidate();
        return "index";
    }

    //GetMapping request til "/" som er index
    @GetMapping("/")
    public String index() {
        return "index";
    }

    //GetMapping request til "/login"
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //PostMapping request til "/login"
    @PostMapping("/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("userPassword") String userPassword,
                        // @RequestParam("userName") annotationen bliver brugt til at binde værdien af requestparameteren til String userName.
                        // så når @PostMapping requesten bliver sendt til /login endpointet så vil userName String objektet
                        // få værdien vi taster ind i @requestparam("userName")
                        HttpSession session, // session parameter bliver brugt til at gemme user objekt senere i metoden
                        Model model) { // Model bliver brugt til at videresende data så det kan blive vist

        // Metoden validerer på om brugernavnet og koden der bliver tastet når man logger ind, stemmer overens med det i databasen.
        User user = accountService.getUserByUserNameAndPassword(userName, userPassword);
        // hvis brugerens login passer, og brugeren ikke er != null. Så kommer vi ind i if - loopet
        if (user != null) {
            session.setAttribute("user", user); // hvis brugeren findes, og brugernavn+kodeord matcher. Så sender vi user, med i sessions som "user" på den bekræftede bruger.
            // vi sætter det maks inaktive interval på at være 15 min. ellers så bliver ens session invalideret.
            session.setMaxInactiveInterval(900);
            // også redirected til "/frontpage" endpointet.
            return "redirect:/frontpage";
        }
        // wrong login info - her tilføjer vi attributen "wrongLoginInfo", og sætter den til true,
        // og ved hjælp af thymeleaf så viser den beskeden, der er sat til "wrongLoginInfo" // hvilket er = "Wrong username or password"
        model.addAttribute("wrongLoginInfo", true);
        return "login";
    }


    //GetMapping request til "/createuser"
    @GetMapping("/createuser")
    public String createUser(Model model) {
        User user = new User(); // vi opretter et nyt User objekt (en ny bruger)
        model.addAttribute("user", user); // det oprettede user objekt gemmes som "user" i model.
        model.addAttribute("roles", Role.values()); // vi tilføjer vores enum klasse Roles værdier, (Role.values() returnerer et array af værdierne 'Role')
        // da det fungerer som en dropdown i programmet når man opretter en bruger og skal vises
        return "createUser"; // henter templaten "/createUser" så man kan oprette en bruger og viser model - user + role.values
    }


    @PostMapping("/createuser")
    public String createdUser(@ModelAttribute("user") User user) { // vi giver @ModelAttribute("user") User user med for at binde den submittede data til den oprettede User
        accountService.createUser(user); // kalder til accountService.createUser, hvor der så oprettes en bruger
        return "createUserSuccess"; // returner os til siden der viser os account info - set bort fra password.
    }

    @GetMapping("/youraccount")
    public String yourAccount(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        user = accountService.getUserById(user.getUserID());
        model.addAttribute("user", user);
        return isLoggedIn(session) ? "accountinfo" : "login";
    }

    @GetMapping("/deleteaccount")
    public String deleteAccount(HttpSession session, @RequestParam("id") int id) {
        accountService.deleteAccount(id);
        session.invalidate();
        return "deleteuser";
    }

    @PostMapping("/deleteaccount")
    public String deleteAccount(@RequestParam String userName,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {
        User user = accountService.getUserByUserNameAndPassword(userName, password);
        accountService.deleteAccount(user.getUserID());
        session.invalidate();
        return "redirect:/index";
    }

    @GetMapping("/editaccount")
    public String editAccount(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        user = accountService.getUserById(user.getUserID());
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "editaccount";
    }

    @PostMapping("editaccount")
    public String editedAccount(HttpSession session, User editedUser) {
        User user = (User) session.getAttribute("user");
        accountService.editAccount(user.getUserID(), editedUser);
        return "redirect:/frontpage";
    }


}
