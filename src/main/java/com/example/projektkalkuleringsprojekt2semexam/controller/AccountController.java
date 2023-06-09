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
        model.addAttribute("user", user); // for at få adgang til en brugers attributer
        model.addAttribute("roles", Role.values()); // vi tilføjer vores enum klasse Roles værdier, (Role.values() returnerer et array af værdierne 'Role')
        // da det fungerer som en dropdown i programmet når man opretter en bruger og skal vises
        return "createUser"; // henter templaten "/createUser" så man kan oprette en bruger og viser model - user + role.values
    } // dette er den gamle metode


    @PostMapping("/createuser")
    public String createdUser(@ModelAttribute("user") User user, Model model) {
        // @ModelAttribute annotationen bruges til at binde den submittede form data (oprettelse af bruger) til User objektet.
        // "user" matcher attributnavnet for User objektet user, så de bindes.
        // grunden til vi bruger @ModelAttribute og ikke @RequestParam er nemlig fordi @MA tillader os at binde flere parametre til et model objekt hvorimod en @RP kun tager 1.
        String userName = user.getUserName(); // opretter en string userName, beder den om at hente den user vi opretters userName
        if (accountService.doesUsernameExist(userName)) { // og tjekker her på om det brugernavn vi lige har tastet ind for at oprette en ny bruger allerede eksisterer.
            model.addAttribute("userNameExists", true); // så bliver beskeden "userNameExists" sat til at være true, og printer så beskeden ud.
            model.addAttribute("roles", Role.values()); // vi tilføjer vores enum klasse Roles værdier, (Role.values() returnerer et array af værdierne 'Role')
            // da det fungerer som en dropdown i programmet når man opretter en bruger og skal vises.
            return "createUser"; // vi returnes, og bedes om at prøve igen med et nyt brugernavn
        } else {
            accountService.createUser(user); // hvis ikke brugernavnet eksisterer, så oprettes brugeren og man dirigeres videre til "createUserSuccess"
            return "createUserSuccess";
        }
    }

    @GetMapping("/deleteaccount")
    public String deleteAccount(HttpSession session) {
        User user = (User) session.getAttribute("user"); // vi det vi har gemt/sendt med i sessions. Her er det så User objektet.
        accountService.deleteAccount(user.getUserID()); // tager det userID der er autogeneret og bundet til den user som vi har givet med i den session
                                                        // man har været logget på med, også sletter den ved hjælp af det.
                                                        // den kalder på accountService.deleteAccount og får userID'et med. Og derefter slettes den
                                                        // inde i vores database ved hjælp af vores repository.
        session.invalidate(); // invaliderer sessionen
        return "redirect:/"; // redirectes til index/startside
    }

    @GetMapping("/editaccount")
    public String editAccount(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user"); // vi det vi har gemt/sendt med i sessions. Her er det så User objektet.
        user = accountService.getUserById(user.getUserID()); // fra det User objekt vi fik med i sessions. Så får vi det userID bundet til den user vi gav med i sessions
                                                            // hvilket er den vi loggede ind med. Så vores
        model.addAttribute("user", user); // Vi tilføjer vores user som vi fik med fra vores session til model, da vi skal vise alt dataen der hører til den bruger
                                            // da det er editAccount og man skal kunne se de informationer man evt. vil ændre.
        model.addAttribute("roles", Role.values()); // (Role.values() returnerer et array af værdierne 'Role') for at vise rollerne hvis man vil ændre dem.
        return "editaccount";
    }

    @PostMapping("editaccount")
    public String editedAccount(HttpSession session, User editedUser) {
        User user = (User) session.getAttribute("user"); // her tager vi det User objekt vi sendte med i sessions tidligere.
        accountService.editAccount(user.getUserID(), editedUser); // vi returner også den nye User som editedUser
        // Her kalder metoden til accountService.editAccount og beder den om at ændre brugeren
        // med det userID der er forbundet til den bruger vi er logget ind med. Muligt pga. sessions.
        return "redirect:/frontpage";
    }


}
