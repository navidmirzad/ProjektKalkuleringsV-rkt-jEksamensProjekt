package com.example.projektkalkuleringsprojekt2semexam.model;

public class Project {

    private int projectid;
    private String projectName;
    private String description;
    private String ImageURL;
    private int estimatedTime;
    private String startDate;
    private String endDate;
    private int projectRank;
    private boolean isDone;

    public Project() {

    }

    public Project(int projectid, String projectName, String description, String imageURL, int estimatedTime, String startDate, String endDate, int projectRank, boolean isDone) {
        this.projectid = projectid;
        this.projectName = projectName;
        this.description = description;
        this.ImageURL = imageURL;
        this.estimatedTime = estimatedTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectRank = projectRank;
        this.isDone = isDone;
    }

    public int getProjectid() {
        return projectid;
    }

    public void setProjectid(int projectid) {
        this.projectid = projectid;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
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
                "projectID=" + projectid +
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
