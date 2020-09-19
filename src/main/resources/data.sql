INSERT INTO users(id, username, first_name, last_name, registration_date, password, email)
VALUES (1, 'firstUser', 'Yura', 'First last name', sysdate(), 'password', 'test4@gmail.com'),
       (2, 'secondUser', 'Liza', 'Second last name', sysdate(), 'password', 'test3@gmail.com'),
       (3, 'thirdUser', 'Anton', 'Third last name', sysdate(), 'password', 'test2@gmail.com'),
        (4, 'test', 'test', 'test', sysdate(), '$2a$10$UL0oHA21nLWCrUknMKamR.sstth5KgFA6qukhoNbRH4pKXPlLYDXO', 'test@gmail.com'),
        (5, 'test1', 'test', 'test', sysdate(), '$2a$10$UL0oHA21nLWCrUknMKamR.sstth5KgFA6qukhoNbRH4pKXPlLYDXO', 'test5@gmail.com');

INSERT INTO project(id, name)
VALUES(1, 'Java Project'),
       (2, 'SQL Project'),
       (3, 'Angular project');

INSERT INTO task(id, title, description, create_date, finish_date, active, project_id, priority)
VALUES (1, 'Create ToDoApp', 'First description', sysdate(), sysdate(), 0, 1, 1),
       (2, 'Learn Angular', 'Second description', sysdate(), sysdate(), 0, 2, 2),
       (3, 'Go for a walk', 'Third description', sysdate(), sysdate(), 1, 3, 3),
       (4, 'Buy chocolate', 'Fourth description', sysdate(), sysdate(), 1, 1, 2);

INSERT INTO task(id,title, create_date,  active, project_id, priority)
VALUES (5, 'Buy pepsi', sysdate(), 1, 1, 2);


INSERT INTO user_project(user_id, project_id)
VALUES (4, 1),
       (1, 2),
       (4, 3),
       (1, 3),
       (2, 3),
       (4, 2);


