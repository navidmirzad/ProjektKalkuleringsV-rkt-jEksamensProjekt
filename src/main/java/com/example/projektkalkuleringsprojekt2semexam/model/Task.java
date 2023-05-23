package com.example.projektkalkuleringsprojekt2semexam.model;

public class Task extends Project {

    private int subProjectID;

    public Task() {
    }

    public Task(int projectID, String projectName, String description, int estimatedTime, int subProjectID) {
        super(projectID, projectName, description, estimatedTime);
        this.subProjectID = subProjectID;
    }

    public int getSubProjectID() {
        return subProjectID;
    }

    public void setSubProjectID(int subProjectID) {
        this.subProjectID = subProjectID;
    }

    public String toString() {
        return "Task name: " + getProjectName() + "\nDescription: " + getDescription() + "\nEstimated time: " + getEstimatedTime() + "\n";
    }



}
