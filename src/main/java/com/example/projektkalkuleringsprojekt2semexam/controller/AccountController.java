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

    private AccountService accountService;

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // invalidate session and return to login page
        session.invalidate();
        return "index";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("userPassword") String userPassword,
                        HttpSession session,
                        Model model) {
        // find user in repo - return loggedIn if succes
        User user = accountService.getUserByUserNameAndPassword(userName, userPassword);
        if (user != null) {

            // create session for user and set session timeout to 15min (container default: 15 min)
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(900);
            return "redirect:/frontpage";
        }
        // wrong login info
        model.addAttribute("wrongLoginInfo", true);
        return "login";
    }


    @GetMapping("/createuser")
    public String createUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "createUser";
    }

    @PostMapping("/createuser")
    public String createdUser(@ModelAttribute("user") User user, Model model) {
        String userName = user.getUserName();
        if (accountService.doesUsernameExist(userName)) {
            model.addAttribute("userNameExists", true);
            model.addAttribute("roles", Role.values());
            return "createUser";
        } else {
            accountService.createUser(user);
            return "createUserSuccess";
        }
    }

    @GetMapping("/youraccount")
    public String yourAccount(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        user = accountService.getUserById(user.getUserID());
        model.addAttribute("user", user);
        return isLoggedIn(session) ? "accountinfo" : "login";
    }

    @GetMapping("/deleteaccount")
    public String deleteAccount(HttpSession session) {
        User user = (User) session.getAttribute("user");
        accountService.deleteAccount(user.getUserID());
        session.invalidate();
        return "redirect:/";
    }

    /*@PostMapping("/deleteaccount")
    public String deleteAccount(HttpSession session) {
        User user = (User) session.getAttribute("user");
        accountService.deleteAccount(user.getUserID());
        session.invalidate();
        return "redirect:/";
    }*/

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
