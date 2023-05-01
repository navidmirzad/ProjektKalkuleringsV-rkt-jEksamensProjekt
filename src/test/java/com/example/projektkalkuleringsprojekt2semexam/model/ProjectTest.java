package com.example.projektkalkuleringsprojekt2semexam.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    List<Project> projects = List.of(new Project(1,"Project1","Desc for project1",15,
                    "2023-02-02","2023-03-03",1,true),
            new Project(1,"Project2","Desc for project2",37,
                    "2023-01-01","2023-13-12",1,true));

    @Test
    public void lengthOfProjectTest() {
        double lengthOfProject = 0;
        for (Project project : projects) {
            lengthOfProject += project.getEstimatedTime();
        }
        assertEquals(52,lengthOfProject);
    }

}