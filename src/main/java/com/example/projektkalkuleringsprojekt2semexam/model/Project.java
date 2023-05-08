package com.example.projektkalkuleringsprojekt2semexam.model;

import java.util.Date;

public class Project {

    private int projectID;
    private String projectName;
    private String description;
    private String ImageURL;
    private int estimatedTime;
    private Date startDate;
    private Date endDate;
    private int projectRank;
    private boolean isDone;

    public Project() {

    }

    public Project(int projectID, String projectName, String description, String imageURL,
                   int estimatedTime, Date startDate, Date endDate, int projectRank, boolean isDone) {
        this.projectID = projectID;
        this.projectName = projectName;
        this.description = description;
        this.ImageURL = imageURL;
        this.estimatedTime = estimatedTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectRank = projectRank;
        this.isDone = isDone;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getProjectRank() {
        return projectRank;
    }

    public void setProjectRank(int projectRank) {
        this.projectRank = projectRank;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectID=" + projectID +
                ", projectName='" + projectName + '\'' +
                ", description='" + description + '\'' +
                ", ImageURL='" + ImageURL + '\'' +
                ", estimatedTime=" + estimatedTime +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", projectRank=" + projectRank +
                ", isDone=" + isDone +
                '}';
    }

}
