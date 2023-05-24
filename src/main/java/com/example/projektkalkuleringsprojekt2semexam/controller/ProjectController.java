

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
                                HttpSession session,
                                @RequestParam List<Integer> listOfUsers) {
        User user = (User) session.getAttribute("user");
        projectService.createProject(project, listOfUsers);
        return "redirect:/createdProject";
    }

    @GetMapping("/createdProject")
    public String insideCreatedProject(HttpSession session) {
        return isLoggedIn(session) ? "insideproject" : "index";
    }

    @GetMapping("/editProject/{projectID}")
    public String editProject(Model model, HttpSession session, @PathVariable int projectID) {
        User user = (User) session.getAttribute("user");
        List<User> users = projectService.getUsers();

        if (isLoggedIn(session)) {
            Project project = projectService.findProjectByID(projectID);
            model.addAttribute("project", project);
            model.addAttribute("users", users);
            model.addAttribute("usersProjects", projectService.getProjectsByUserId(user.getUserID()));
        } return isLoggedIn(session) ? "editProject" : "index";
    }

    @PostMapping("/editProject/{projectID}")
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
        session.setAttribute("projectID", projectID);
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
        User user = (User) session.getAttribute("user");
        int projectID = (int) session.getAttribute("projectID");

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
        int projectid = (int) session.getAttribute("projectID");

        return "redirect:/seeproject/" + projectid;
    }

    @PostMapping("/deletesubproject")
    public String deleteSubproject(@RequestParam("id") int id, HttpSession session) {
        int projectid = (int) session.getAttribute("projectID");
        projectService.deleteSubproject(id);
        return "redirect:/seeproject/" + projectid;
    }

    @GetMapping("createtask/{subprojectid}")
    public String createTask(@PathVariable("subprojectid") int subprojectid, Model model, HttpSession session) {
        Task task = new Task();
        List<User> users = projectService.getUsersBySubpojectId(subprojectid);
        model.addAttribute("task", task);
        model.addAttribute("subprojectid", subprojectid);
        model.addAttribute("users", users);
        return "createtask";
    }

    @PostMapping("createtask/{subprojectid}")
    public String createTask(@PathVariable("subprojectid") int subprojectid, HttpSession session,
                             @ModelAttribute Task task,
                             @RequestParam List<Integer> listOfUsers) {

        User user = (User) session.getAttribute("user");
        int projectID = (int) session.getAttribute("projectID");

        projectService.createTask(listOfUsers, subprojectid, task);

        return "redirect:/seeproject/" + projectID;
    }

    @PostMapping("/deletetask")
    public String deleteTask(@RequestParam("taskId") int taskId,
                             HttpSession session) {
        int projectID = (int) session.getAttribute("projectID");
        projectService.deleteTask(taskId);
        return "redirect:/seeproject/" + projectID;
    }

    @GetMapping("/edittask/{taskId}")
    public String editTask(@PathVariable("taskId") int taskId, Model model) {

        Task task = projectService.getTaskById(taskId);
        int subprojectId = projectService.getSubprojectIdByTaskId(taskId);
        List<User> users = projectService.getUsersBySubpojectId(subprojectId);
        model.addAttribute("task", task);
        model.addAttribute("users", users);

        return "edittask";
    }

    @PostMapping("edittask/{taskId}")
    public String editTask(@PathVariable int taskId,
                           @ModelAttribute Task editedTask,
                           HttpSession session,
                           @RequestParam List<Integer> listOfUsers) {

        int projectID = (int) session.getAttribute("projectID");

        projectService.editTask(taskId, editedTask, listOfUsers);

        return "redirect:/seeproject/" + projectID;
    }
}



