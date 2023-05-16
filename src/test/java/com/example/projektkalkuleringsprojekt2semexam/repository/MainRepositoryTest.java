package com.example.projektkalkuleringsprojekt2semexam.repository;

import com.example.projektkalkuleringsprojekt2semexam.controller.AccountController;
import com.example.projektkalkuleringsprojekt2semexam.model.Project;
import com.example.projektkalkuleringsprojekt2semexam.model.Role;
import com.example.projektkalkuleringsprojekt2semexam.model.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainRepositoryTest {

    @Test
    public void canCreateProject() {
        List<Project> projects = new ArrayList<>();
        Project project = new Project(1, "newProject", "a project", "project.com", 22,
                Date.from(Instant.now()), Date.from(Instant.now()), 1);

        projects.add(project);

        assertEquals(projects.toArray().length, 1);
        System.out.println(projects);
    }

    @Test
    public void canSetProjectID() {
        Project project = new Project();
        project.setProjectID(22);

        assertEquals(project.getProjectID(), 22);
    }


}