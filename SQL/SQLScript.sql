CREATE DATABASE projectcalculatorDB;
USE projectcalculatorDB;

CREATE TABLE user (
userid INT PRIMARY KEY AUTO_INCREMENT,
firstName VARCHAR(255) NOT NULL,
lastName VARCHAR(255) NOT NULL,
username VARCHAR(255) NOT NULL,
password VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL,
birthDate DATE NOT NULL,
phoneNumber INT NOT NULL,
ROLE ENUM('admin','leader','contributer')
);

CREATE TABLE project (
projectid INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(255) NOT NULL,
description TEXT NOT NULL,
estimatedTime VARCHAR(255) NOT NULL,
startDate DATE NOT NULL,
endDate DATE NOT NULL,
projectRank INT NOT NULL,
isDone BOOLEAN NOT NULL
);

CREATE TABLE users_projects (
userid INT NOT NULL,
projectid INT NOT NULL,
FOREIGN KEY (userid) REFERENCES user(userid),
FOREIGN KEY (projectid) REFERENCES project(projectid)
);
