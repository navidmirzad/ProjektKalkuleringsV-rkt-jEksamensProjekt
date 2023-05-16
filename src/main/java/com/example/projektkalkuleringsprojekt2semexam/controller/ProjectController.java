

package com.example.projektkalkuleringsprojekt2semexam.controller;

import com.example.projektkalkuleringsprojekt2semexam.model.Project;
import com.example.projektkalkuleringsprojekt2semexam.model.User;
import com.example.projektkalkuleringsprojekt2semexam.service.ProjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProjectController {

    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    // showProjects method or frontpage GetMapping lets us create project, make HttpSession with userID and shows projects
    @GetMapping("/frontpage")
    public String showProjects(Model model, HttpSession session) {
        Project project = new Project();
        User user = (User) session.getAttribute("user");

        if (isLoggedIn(session)) {
            model.addAttribute("projects", projectService.getProjectsByUserId(user.getUserID()));
            model.addAttribute("project", project);
        }
        return isLoggedIn(session) ? "frontpage" : "index";
    }

    @PostMapping("/insideproject")
    public String createProject(@ModelAttribute("project") Project project, HttpSession session) {
        User user = (User) session.getAttribute("user");
        projectService.createProject(project, user.getUserID());
        return "redirect:/createdProject";
    }

    @GetMapping("/createdProject")
    public String insideCreatedProject(HttpSession session) {
        return isLoggedIn(session) ? "insideproject" : "index";
    }

    @GetMapping("/editProject/{id}")
    public String editProject(@PathVariable int id, Model model, HttpSession session, Project editedProject) {
        projectService.editProject(id, editedProject);
        User user = (User) session.getAttribute("user");

        model.addAttribute("project", editedProject);
        if (isLoggedIn(session)) {
            model.addAttribute("usersProjects", projectService.getProjectsByUserId(user.getUserID()));
        } return isLoggedIn(session) ? "frontpage" : "index";
    }

    @PostMapping("/editProject/{id}")
    public String editedProject(@PathVariable int id, @ModelAttribute Project editedProject) {
        projectService.editProject(id, editedProject);
        return "redirect:/frontpage";
    }

    @PostMapping("/deleteProject")
    public String deleteProject(@RequestParam("id") int id) {
        projectService.deleteProject(id);
        return "redirect:/frontpage";
    }


    @GetMapping("/aboutUs")
    public String aboutUs() {
        return "aboutUs";
    }

}



