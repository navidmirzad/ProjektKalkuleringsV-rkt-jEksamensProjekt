package com.example.projektkalkuleringsprojekt2semexam.controller;

import com.example.projektkalkuleringsprojekt2semexam.model.User;
import com.example.projektkalkuleringsprojekt2semexam.service.ProjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private ProjectService projectService;

    public LoginController(ProjectService projectService) {
        this.projectService = projectService;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
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

    @PostMapping("/") // index is loginPage
    public String index(@RequestParam("userName") String userName,
                        @RequestParam("userPassword") String userPassword,
                        HttpSession session,
                        Model model)
    {
        // find user in repo - return loggedIn if succes
        User user = projectService.getUser(userName);
        if (user != null)
            if (user.getUserPassword().equals(userPassword)) {
                // create session for user and set session timeout to 30 sec (container default: 15 min)
                session.setAttribute("user", user);
                session.setMaxInactiveInterval(300);
                return "frontpage";
            }
        // wrong login info
        model.addAttribute("wrongLoginInfo", true);
        return "index";
    }


    @GetMapping("/frontpage")
    public String frontPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
       // model.addAttribute("projects", projectService.getProjects(user.getUserID()));
        return isLoggedIn(session) ? "frontpage" : "index";

    }



}
