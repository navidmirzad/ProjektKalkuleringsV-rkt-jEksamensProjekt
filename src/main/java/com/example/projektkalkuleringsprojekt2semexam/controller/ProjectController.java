

package com.example.projektkalkuleringsprojekt2semexam.controller;

import com.example.projektkalkuleringsprojekt2semexam.model.Project;
import com.example.projektkalkuleringsprojekt2semexam.model.Subproject;
import com.example.projektkalkuleringsprojekt2semexam.model.Task;
import com.example.projektkalkuleringsprojekt2semexam.model.Subproject;
import com.example.projektkalkuleringsprojekt2semexam.model.User;
import com.example.projektkalkuleringsprojekt2semexam.service.AccountService;
import com.example.projektkalkuleringsprojekt2semexam.service.ProjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        List<User> users = projectService.getUsers();

        if (isLoggedIn(session)) {
            model.addAttribute("projects", projectService.getProjectsByUserId(user.getUserID()));
            model.addAttribute("project", project);
            model.addAttribute("users", users);
        }
        return isLoggedIn(session) ? "frontpage" : "index";
    }

    @PostMapping("/insideproject")
    public String createProject(@ModelAttribute("project") Project project,
                                @RequestParam List<Integer> listOfUsers) {
        projectService.createProject(project, listOfUsers);
        return "redirect:/frontpage";
    }


    @GetMapping("/editproject/{projectID}")
    public String editProject(Model model, HttpSession session, @PathVariable int projectID) {
        User user = (User) session.getAttribute("user");
        List<User> users = projectService.getUsers();

        if (isLoggedIn(session)) {
            Project project = projectService.findProjectByID(projectID);
            model.addAttribute("project", project);
            model.addAttribute("users", users);
            model.addAttribute("usersProjects", projectService.getProjectsByUserId(user.getUserID()));
        } return isLoggedIn(session) ? "editproject" : "index";
    }

    @PostMapping("/editproject/{projectID}")
    public String editedProject(@PathVariable int projectID, @ModelAttribute Project editedProject,
                                @RequestParam List<Integer> listOfUsers) {
        projectService.editProject(projectID, editedProject, listOfUsers);
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

    //subproject

    @GetMapping("/seeproject/{projectID}")
    public String seeProject(@PathVariable int projectID, Model model, HttpSession session) {
        Project project = projectService.findProjectByID(projectID);
        session.setAttribute("project", project);
        Subproject subproject = new Subproject();
        List<Subproject> subprojects = projectService.getSubprojectByProjectId(projectID);
        List<User> users = projectService.getUsersByProjectId(projectID);

        model.addAttribute("subproject",subproject);
        model.addAttribute("subprojects", subprojects);
        model.addAttribute("users", users);

        return "seeproject";
    }

    @PostMapping("/createsubproject")
    public String createSubproject(@ModelAttribute("subproject") Subproject subproject,
                                   HttpSession session,
                                   @RequestParam List<Integer> listOfUsers) {
        Project project = (Project) session.getAttribute("project");
        int projectID = project.getProjectID();
        projectService.createSubproject(listOfUsers, projectID, subproject);

        return "redirect:/seeproject/" + projectID;
    }

    @GetMapping("/editsubproject/{id}")
    public String editSubproject(@PathVariable int id,
                                 Model model) {
        Subproject subproject = projectService.getSubprojectById(id);
        int projectId = projectService.getProjectIdBySubprojectId(id);
        List<User> users = projectService.getUsersByProjectId(projectId);

        model.addAttribute("users", users);
        model.addAttribute("subproject", subproject);

        return "editsubproject";
    }

    @PostMapping("/editsubproject/{id}")
    public String editSubproject(@PathVariable int id,
                                 @ModelAttribute Subproject editedSubproject,
                                 HttpSession session,
                                 @RequestParam List<Integer> listOfUsers) {
        projectService.editSubproject(id, editedSubproject,listOfUsers);
        Project project = (Project) session.getAttribute("project");
        int projectID = project.getProjectID();

        return "redirect:/seeproject/" + projectID;
    }

    @PostMapping("/deletesubproject")
    public String deleteSubproject(@RequestParam("id") int id, HttpSession session) {
        Project project = (Project) session.getAttribute("project");
        int projectID = project.getProjectID();
        projectService.deleteSubproject(id);
        return "redirect:/seeproject/" + projectID;
    }

    @GetMapping("createtask/{subprojectID}")
    public String createTask(@PathVariable("subprojectID") int subprojectID, Model model, HttpSession session) {
        Task task = new Task();
        List<User> users = projectService.getUsersBySubpojectId(subprojectID);
        model.addAttribute("task", task);
        model.addAttribute("subprojectID", subprojectID);
        model.addAttribute("users", users);
        return "createtask";
    }

    @PostMapping("createtask/{subprojectID}")
    public String createTask(@PathVariable("subprojectID") int subprojectID, HttpSession session,
                             @ModelAttribute Task task,
                             @RequestParam List<Integer> listOfUsers) {

        User user = (User) session.getAttribute("user");
        Project project = (Project) session.getAttribute("project");
        int projectID = project.getProjectID();

        projectService.createTask(listOfUsers, subprojectID, task);

        return "redirect:/seeproject/" + projectID;
    }

    @PostMapping("/deletetask")
    public String deleteTask(@RequestParam("taskID") int taskID,
                             HttpSession session) {
        Project project = (Project) session.getAttribute("project");
        int projectID = project.getProjectID();
        projectService.deleteTask(taskID);
        return "redirect:/seeproject/" + projectID;
    }

    @GetMapping("/edittask/{taskID}")
    public String editTask(@PathVariable("taskID") int taskID, Model model) {

        Task task = projectService.getTaskById(taskID);
        int subprojectId = projectService.getSubprojectIdByTaskId(taskID);
        List<User> users = projectService.getUsersBySubpojectId(subprojectId);
        model.addAttribute("task", task);
        model.addAttribute("users", users);

        return "edittask";
    }

    @PostMapping("edittask/{taskID}")
    public String editTask(@PathVariable int taskID,
                           @ModelAttribute Task editedTask,
                           HttpSession session,
                           @RequestParam List<Integer> listOfUsers) {

        Project project = (Project) session.getAttribute("project");
        int projectID = project.getProjectID();

        projectService.editTask(taskID, editedTask, listOfUsers);

        return "redirect:/seeproject/" + projectID;
    }
}



