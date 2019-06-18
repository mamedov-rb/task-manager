

INSERT INTO app_user(id, version, username, password, full_name, age, email, phone)
VALUES ('userID_1', 1, 'user_001', '$2a$10$kHSKOmw/k7vh08cvVh1/x.rMfwPGEmlpCbn/zflixbkFG3pq3ZBCC', 'John Travolta', 40,
        'johnTravolta@mail.ru', '+7(900)800-70-07');
INSERT INTO app_user(id, version, username, password, full_name, age, email, phone)
VALUES ('userID_2', 1, 'user_002', '$2a$10$kHSKOmw/k7vh08cvVh1/x.rMfwPGEmlpCbn/zflixbkFG3pq3ZBCC', 'Albert Enstain', 80,
        'albertEnstain@mail.ru', '+7(900)800-70-07');

INSERT INTO role (id, version, name)
VALUES ('roleID_1', 1, 'ROLE_ADMIN');
INSERT INTO role (id, version, name)
VALUES ('roleID_2', 1, 'ROLE_DEVELOPER');
INSERT INTO role (id, version, name)
VALUES ('roleID_3', 1, 'ROLE_TESTER');

INSERT INTO users_roles (user_id, role_id)
VALUES ('userID_1', 'roleID_1');
INSERT INTO users_roles (user_id, role_id)
VALUES ('userID_1', 'roleID_2');
INSERT INTO users_roles (user_id, role_id)
VALUES ('userID_1', 'roleID_3');
INSERT INTO users_roles (user_id, role_id)
VALUES ('userID_2', 'roleID_2');
INSERT INTO users_roles (user_id, role_id)
VALUES ('userID_2', 'roleID_3');

INSERT INTO project (id, version, name, description, created, start_date, end_date, status)
VALUES ('projectID_1', 1, 'Project_1', 'Some description of project #1.', '2019-04-19', '2019-07-10', '2020-10-10',
        'PLANNED');
INSERT INTO project (id, version, name, description, created, start_date, end_date, status)
VALUES ('projectID_2', 1, 'Project_2', 'Some description of project #2.', '2019-05-01', '2019-03-01', '2020-10-01',
        'PLANNED');

INSERT INTO task (id, version, name, description, created, start_date, end_date, status, project_id, user_id)
VALUES ('taskID_1', 1, 'Task_1', 'Some task description of task #1', '2019-04-19', '2019-07-15', '2019-08-25', 'PLANNED',
        'projectID_1', 'userID_1');
INSERT INTO task (id, version, name, description, created, start_date, end_date, status, project_id, user_id)
VALUES ('taskID_2', 1, 'Task_2', 'Some task description of task #2', '2019-04-19', '2019-08-16', '2019-09-26', 'DONE',
        'projectID_1', 'userID_1');
INSERT INTO task (id, version, name, description, created, start_date, end_date, status, project_id, user_id)
VALUES ('taskID_5', 1, 'Task_5', 'Some task description of task #5', '2019-04-19', '2019-08-16', '2019-09-26',
        'IN_PROGRESS', 'projectID_1', 'userID_1');
INSERT INTO task (id, version, name, description, created, start_date, end_date, status, project_id, user_id)
VALUES ('taskID_6', 1, 'Task_6', 'Some task description of task #6', '2019-04-19', '2019-08-16', '2019-09-26', 'PLANNED',
        'projectID_1', 'userID_1');
INSERT INTO task (id, version, name, description, created, start_date, end_date, status, project_id, user_id)
VALUES ('taskID_7', 1, 'Task_7', 'Some task description of task #7', '2019-04-19', '2019-08-16', '2019-09-26', 'PLANNED',
        'projectID_1', 'userID_1');
INSERT INTO task (id, version, name, description, created, start_date, end_date, status, project_id, user_id)
VALUES ('taskID_3', 1, 'Task_3', 'Some task description of task #2', '2019-04-19', '2019-08-16', '2019-09-26', 'PAUSED',
        'projectID_2', 'userID_2');
INSERT INTO task (id, version, name, description, created, start_date, end_date, status, project_id, user_id)
VALUES ('taskID_4', 1, 'Task_4', 'Some task description of task #3', '2019-04-19', '2019-08-16', '2019-09-26', 'DONE',
        'projectID_2', 'userID_2');

INSERT INTO users_projects (user_id, project_id)
VALUES ('userID_1', 'projectID_1');
INSERT INTO users_projects (user_id, project_id)
VALUES ('userID_2', 'projectID_2');