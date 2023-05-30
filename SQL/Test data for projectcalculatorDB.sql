INSERT INTO User (firstName, lastName, userName, userPassword, email, birthDate, phoneNumber, Role)
VALUES 
("Mathias", "Wulff", "mathias", 123, "mw@mail.com", "1998/1/1", 1234, 'admin'),
("Enes", "Kocer", "enes", 123, "mw@mail.com", "2000/2/2", 5678, 'leader'),
("Navid", "Mirzad", "nav", 123, "nm@mail.com", "2001/2/2", 9012, 'contributer');

INSERT INTO project (projectName, description, imageURL, estimatedTime, startDate, endDate)
VALUES
("Eksamensprojekt", "lave et system for AlphaSolutions", "https://rb.gy/roaih", 100, "2023/04/24", "2023/05/31");

INSERT INTO users_projects (userID, projectID)
VALUES
(1, 1),
(2, 1),
(3, 1);

INSERT INTO subproject (subprojectName, description, estimatedTime, projectID) 
VALUES
("Få skrevet rapporten", "Vi skal have skrevet mindst 40 sider, og maximum 80 fordi vi en gruppe på 3", 50, 1),
("Få koden og funktionalitet til at fungerer", "Skrive kode", 100, 1),
("User interface", "Skal se godt ud", 20, 1);

INSERT INTO users_subprojects (userID, subprojectID) 
VALUES
(1, 1),
(2, 1),
(3, 1),
(1, 2),
(2, 2),
(3, 2),
(1, 3),
(2, 3),
(3, 3);

INSERT INTO task (taskName, description, estimatedTime, subprojectID) 
VALUES
("Virksomhedsdelen", "feasibility, risikoplan, og interessentanalyse", 10, 1),
("Huske bilag + litteraturliste", "titel", 10, 1),
("CRUD på oprettelse af project", "create, read, update, delete", 15, 2),
("Forbinde databasen", "JDBC API", 2, 2),
("10 UI Heurestikker", "evt spørg Lenka om hjaelp", 8, 3);

INSERT INTO users_tasks (userID, taskID) 
VALUES

# task til 'virksomhedsdelen'
(1, 1),
(2, 1),
(3, 1),

# Task til bilag + litteraturliste
(1, 2),
(2, 2),
(3, 2),

# Task til CRUD på project
(1, 3),
(2, 3),
(3, 3),

# Task til Forbinde Database 
(1, 4),
(2, 4),
(3, 4),

(1, 5),
(2, 5),
(3, 5);