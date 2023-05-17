package com.example.projektkalkuleringsprojekt2semexam.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    List<Project> projects = List.of(new Project(1,"Project1","Desc for project1","google.com", 15,
                    Date.from(Instant.now()), Date.from(Instant.now())),
            new Project(1,"Project2","Desc for project2", "facebook.com", 37,
                    Date.from(Instant.now()),Date.from(Instant.now())));

    @Test
    public void lengthOfProjectTest() {
        double lengthOfProject = 0;
        for (Project project : projects) {
            lengthOfProject += project.getEstimatedTime();
        }
        assertEquals(52,lengthOfProject);
    }

}