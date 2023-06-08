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

    // opretter et projectservice objekt, så vi kan få adgang til metoderne i klassen
    private ProjectService projectService;


    // konstruktør for at oprette en instans af projectservice
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Boolean metode der checker på om man er logget ind med en parameteren HttpSession session
    private boolean isLoggedIn(HttpSession session) {
        // session.getAttribute("user") henter den værdi der er gemt i HttpSession objektet under attribute navnet "user".
        // hvis user ikke er null
        return session.getAttribute("user") != null;
    }

    // showProjects method or frontpage GetMapping lets us create project, make HttpSession with userID and shows projects
    @GetMapping("/frontpage")
    public String showProjects(Model model, HttpSession session) { // giver Model, og HttpSession med som parameter
        Project project = new Project(); // opretter en ny instans af Project
        // Her henter vi værdien gemt i HttpSession under attribut navnet "user". Og den går ud fra at objektet er en User og caster den til User.
        User user = (User) session.getAttribute("user");
        // Vi har en liste med objektet User kaldt users, og kalder så metoden getUsers fra projectService,
        // som viderekalder til projectRepository.getUsers som så henter en liste af vores brugere fra databasen
        List<User> users = projectService.getUsers();

        if (isLoggedIn(session)) { // her tjekker vi om vi er logget ind, og hvis vi er så får vi session paramteren med. For at få adgang til user
            model.addAttribute("projects", projectService.getProjectsByUserId(user.getUserID())); // tilføjer alle de projekter tilhørende til det userID vi får med fra sessions. Så de kan blive vist på frontpage
            model.addAttribute("project", project); // her tilføjer vi de nye oprettede projekter til model, så de kan blive vist. På siden
            model.addAttribute("users", users); // Her tilføjer vi listen af brugere (users) med labelet "users" til model, så når man opretter et projekt kan tilføje medlemmer
        }
        return isLoggedIn(session) ? "frontpage" : "index";
    }


    @GetMapping("/editproject/{projectID}")
    public String editProject(Model model, HttpSession session, @PathVariable int projectID) { //@PathVariable annotation bruger vores URi url {projectID} til at give
                                                                                            // int projectID'et dens værdi.
        User user = (User) session.getAttribute("user"); // tager user med i vores session attribut så vi kan få adgang til User objektet.
        List<User> users = projectService.getUsers(); // henter en liste af alle brugere i vores program.

        if (isLoggedIn(session)) {
            Project project = projectService.findProjectByID(projectID); // her kalder vi på projectService.findProjectByID og giver (projectID) med i paramteren
                                                                        // her henter vi ID'et på det enkelte project, så vi senere i vores repository metode
                                                                        // ved hvilken et af projekterne der skal edites i
            // {projectID} sætter @PathVariable int projectID værdi. Det gør vi vha. thymeleaf,
            // som finder {projectID} ud fra vores link = <a th:href="@{/editproject/{projectID}(projectID=${project.projectID})}"
            model.addAttribute("project", project); // også fordi vi tilføjer det project til model, så vi kan se og vise informationen om projektet
            model.addAttribute("users", users); // her tilføjer vi listen af users til model, så vi vi kan tilføje og fravælge hvem der skal kunne se projektet
            model.addAttribute("usersProjects", projectService.getProjectsByUserId(user.getUserID())); // her laver vi en attribut kaldt "usersProjects"
                                                                    // som kalder på projectService og kalder en metode der henter alle de projects der hører til
                                                                    // det userID som vi er logget ind med, og fået fra HttpSession.
        } return isLoggedIn(session) ? "editproject" : "index";
    }

    @PostMapping("/editproject/{projectID}")
    public String editedProject(@PathVariable int projectID, @ModelAttribute Project editedProject, // @PathVariable int projectID får sin værdi fra {projectID} URL'en
                                // @ModelAttribute Project editedProject, binder den data vi ændrer i Project objected "editedProject"
                                @RequestParam List<Integer> listOfUsers) { // @RequestParam sørger for at binde de brugere vi har tilføjet/fjernet fra projektet
                                                                        // til vores list<Integer> listOfUsers
        projectService.editProject(projectID, editedProject, listOfUsers); // her kalder vi projectService.editProject, og giver den parameterne, projectID,
                                                                            // det ændrede Project object "editedProject", og den opdaterede liste af users på projektet.
        return "redirect:/frontpage";
    }


    @PostMapping("/deleteProject")
    public String deleteProject(@RequestParam("id") int id) { // annotationen bruges til at binde @RequestParam "id", til int id.
        projectService.deleteProject(id); // kalder vi projectService metoden, deleteProject og giver den (id)'et med som parameter
        return "redirect:/frontpage";
    }

    @GetMapping("/aboutUs")
    public String aboutUs() {
        return "aboutUs";
    }

    //subproject

    @GetMapping("/seeproject/{projectID}")
    public String seeProject(@PathVariable int projectID, Model model, HttpSession session) { // @PathVariable int projectID får sin værdi fra {projectID}, model + session
        Project project = projectService.findProjectByID(projectID); // project, kalder projectService.FindProjectByID(projectID) - som er værdien vi får med fra vores @PathVariable
        session.setAttribute("project", project); // her sørger vi for at project objektet kommer med i vores session
        Subproject subproject = new Subproject(); // her opretter vi et subproject
        List<Subproject> subprojects = projectService.getSubprojectByProjectId(projectID); //Her har vi en liste med alle de subprojekter der hører til det projectID vi fik i vores @PathVariable
        List<User> users = projectService.getUsersByProjectId(projectID); // En liste med User objekter, kalder projectService som henter alle users tilhørende det projectID

        model.addAttribute("subproject",subproject); // tilføjer subproject til model, så subprojects når man opretter det kan vises
        model.addAttribute("subprojects", subprojects); // tilføjer subprojects til model, så subprojects kan vises inde på seeproject endpointet
        model.addAttribute("users", users); // spørg Mathias/Enes hvorfor vi tilføjer users til model her
        return "seeproject";
    }

    @PostMapping("/createsubproject")
    public String createSubproject(@ModelAttribute("subproject") Subproject subproject, // @ModelAttribute bruges til at binde det oprettede "subproject" Subproject objektet subproject.
                                   HttpSession session,
                                   @RequestParam List<Integer> listOfUsers) { // her binder vi listen af brugere der er tilføjet/ikke tilføjet til projektet til listOfUsers.
        Project project = (Project) session.getAttribute("project"); // her tager vi vores Project object ud af session
        int projectID = project.getProjectID(); // her gør vi så brug af vores project object - da vi skal bruge projectID'et på det project vi er inde i
        projectService.createSubproject(listOfUsers, projectID, subproject); // her opretter vi så et subproject, og til det skal vi bruge
                                                                            // (listen af brugere der er tilføjet til det subproject, projectID
                                                                            // Spørgs Mathias/Enes hvad egenskaben bag subproject her er
        return "redirect:/seeproject/" + projectID;
    }

    @GetMapping("/editsubproject/{id}")
    public String editSubproject(@PathVariable int id, // int id, får sin værdi fra {id} fra URL'en.
                                 Model model) {
        Subproject subproject = projectService.getSubprojectById(id); // vi henter det subproject der er relateret til det id (subprojectID) vi får fra URL'en.
        int projectId = projectService.getProjectIdBySubprojectId(id); // her henter vi det projectID som tilhører det subproject vi vil ændrer
        List<User> users = projectService.getUsersByProjectId(projectId);

        model.addAttribute("users", users); // tilføjer til model, så vi kan vise listen users, så man kan tilføje/fjerne medlemmer
        model.addAttribute("subproject", subproject); // tilføjer subproject til model, så vi kan se subprojectets værdier/info så man ved hvad man ændrer
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



