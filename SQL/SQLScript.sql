CREATE DATABASE projectcalculatorDB;
USE projectcalculatorDB;

CREATE TABLE user (
userID INT PRIMARY KEY AUTO_INCREMENT,
firstName VARCHAR(255) NOT NULL,
lastName VARCHAR(255) NOT NULL,
userName VARCHAR(255) NOT NULL,
userPassword VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL,
birthDate DATE NOT NULL,
phoneNumber INT NOT NULL,
ROLE ENUM('admin','leader','contributer')
);

CREATE TABLE project (
projectID INT PRIMARY KEY AUTO_INCREMENT,
projectName VARCHAR(255) NOT NULL,
description TEXT,
ImageURL VARCHAR(255),
estimatedTime INT,
startDate DATE NOT NULL,
endDate DATE NOT NULL,
projectRank INT NOT NULL,
isDone BOOLEAN
);

CREATE TABLE users_projects (
userID INT NOT NULL,
projectID INT NOT NULL,
PRIMARY KEY (userID, projectID),
FOREIGN KEY (userid) REFERENCES user(userid),
FOREIGN KEY (projectid) REFERENCES project(projectid)
);
