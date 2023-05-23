package com.example.projektkalkuleringsprojekt2semexam.model;

import java.util.ArrayList;
import java.util.List;

public class Subproject extends Project {

    private int superProjectID;
    private List<Task> tasks = new ArrayList<>();

    public Subproject() {
    }

    public Subproject(int projectID, String projectName, String description, int estimatedTime, int superProjectID) {
        super(projectID, projectName, description, estimatedTime);
        this.superProjectID = superProjectID;
    }

    public int getSuperProjectID() {
        return superProjectID;
    }

    public void setSuperProjectID(int superProjectID) {
        this.superProjectID = superProjectID;
    }

    public List<Task> getTasks() {
        return tasks;
    }

}
