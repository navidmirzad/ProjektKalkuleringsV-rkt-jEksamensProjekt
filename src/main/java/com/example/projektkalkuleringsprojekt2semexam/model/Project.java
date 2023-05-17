package com.example.projektkalkuleringsprojekt2semexam.model;

import java.util.Date;

public class Project {

    private int projectID;
    private String projectName;
    private String description;
    private String imageURL;
    private int estimatedTime;
    private Date startDate;
    private Date endDate;
    private int projectRank;

    public Project() {

    }

    public Project(int projectID, String projectName, String description, String imageURL,
                   int estimatedTime, Date startDate, Date endDate, int projectRank) {
        this.projectID = projectID;
        this.projectName = projectName;
        this.description = description;
        this.imageURL = imageURL;
        this.estimatedTime = estimatedTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectRank = projectRank;
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
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public java.sql.Date getStartDate() {
        return (java.sql.Date) startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public java.sql.Date getEndDate() {
        return (java.sql.Date) endDate;
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
    @Override
    public String toString() {
        return "Project{" +
                "projectID=" + projectID +
                ", projectName='" + projectName + '\'' +
                ", description='" + description + '\'' +
                ", ImageURL='" + imageURL + '\'' +
                ", estimatedTime=" + estimatedTime +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", projectRank=" + projectRank +
                '}';
    }

}
