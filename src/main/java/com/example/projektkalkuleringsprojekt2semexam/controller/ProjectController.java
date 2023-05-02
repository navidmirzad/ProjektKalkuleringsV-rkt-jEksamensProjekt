package com.example.projektkalkuleringsprojekt2semexam.controller;

import com.example.projektkalkuleringsprojekt2semexam.model.User;
import com.example.projektkalkuleringsprojekt2semexam.service.ProjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class ProjectController {

    private ProjectService projectService;

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/frontpage")
    public String getProjects(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("wishlists", projectService.getProjects(user.getUserID()));
            return "wishlists";
        } else {
            return "redirect:/wishlist/frontpage";
        }
    }

}
