INSERT INTO app_user (id, username, first_name, last_name, email, phone, password, version, created, updated)
VALUES ('uId-1', 'user_01', 'User-1_first_name', 'User-1_last_name', 'user1@gmail.com', '+7(800)100-10-10',
        '$2a$10$n8.9fTGIq05xuDx1DA.qBORTxXv7VykkBTmAARbxDoYNL0l35q5jm', 0, '2016-11-09T11:44:44.797', '2016-11-09T11:44:44.797');
INSERT INTO app_user (id, username, first_name, last_name, email, phone, password, version, created, updated)
VALUES ('uId-2', 'user_02', 'User-2_first_name', 'User-2_last_name', 'user2@gmail.com', '+7(800)100-10-10',
        '$2a$10$n8.9fTGIq05xuDx1DA.qBORTxXv7VykkBTmAARbxDoYNL0l35q5jm', 0, '2016-11-05T11:44:44.797', '2016-11-05T11:44:44.797');
INSERT INTO app_user (id, username, first_name, last_name, email, phone, password, version, created, updated)
VALUES ('uId-3', 'user_03', 'User-3_first_name', 'User-3_last_name', 'user3@gmail.com', '+7(800)100-10-10',
        '$2a$10$n8.9fTGIq05xuDx1DA.qBORTxXv7VykkBTmAARbxDoYNL0l35q5jm', 0, '2016-11-05T11:44:44.797', '2016-11-05T11:44:44.797');

INSERT INTO project (id, created, description, end_date, name, start_date, updated, version, created_by_id)
VALUES ('projectId_01', '2016-11-05T11:44:44.797', 'Some description of project 1', '2016-11-05T11:44:44.797',
        'project_01', '2016-11-05T11:44:44.797', '2016-11-05T11:44:44.797', 0, 'uId-1');
INSERT INTO project (id, created, description, end_date, name, start_date, updated, version, created_by_id)
VALUES ('projectId_02', '2016-11-05T11:44:44.797', 'Some description of project 2', '2016-11-05T11:44:44.797',
        'project_02', '2016-11-05T11:44:44.797', '2016-11-05T11:44:44.797', 0, 'uId-2');
INSERT INTO project (id, created, description, end_date, name, start_date, updated, version, created_by_id)
VALUES ('projectId_03', '2016-11-05T11:44:44.797', 'Some description of project 3', '2016-11-05T11:44:44.797',
        'project_03', '2016-11-05T11:44:44.797', '2016-11-05T11:44:44.797', 0, 'uId-3');

INSERT INTO users_projects (user_id, project_id) VALUES ('uId-2', 'projectId_01');
INSERT INTO users_projects (user_id, project_id) VALUES ('uId-2', 'projectId_02');
INSERT INTO users_projects (user_id, project_id) VALUES ('uId-3', 'projectId_03');

INSERT INTO task (id, created, description, end_date, name, start_date, status, updated, version, assigned_to_id, created_by_id, project_id)
VALUES ('taskId_01', '2016-11-05T11:44:44.797', 'Some description of task 1', '2016-11-05T11:44:44.797', 'task_01',
        '2016-11-05T11:44:44.797', 'PLANNED', '2016-11-05T11:44:44.797', 0, 'uId-2', 'uId-2', 'projectId_01');
INSERT INTO task (id, created, description, end_date, name, start_date, status, updated, version, assigned_to_id, created_by_id, project_id)
VALUES ('taskId_02', '2016-11-05T11:44:44.797', 'Some description of task 2', '2016-11-05T11:44:44.797', 'task_02',
        '2016-11-05T11:44:44.797', 'PLANNED', '2016-11-05T11:44:44.797', 0, 'uId-2', 'uId-2', 'projectId_01');
INSERT INTO task (id, created, description, end_date, name, start_date, status, updated, version, assigned_to_id, created_by_id, project_id)
VALUES ('taskId_03', '2016-11-05T11:44:44.797', 'Some description of task 3', '2016-11-05T11:44:44.797', 'task_03',
        '2016-11-05T11:44:44.797', 'IN_PROGRESS', '2016-11-05T11:44:44.797', 0, 'uId-2', 'uId-2', 'projectId_01');
INSERT INTO task (id, created, description, end_date, name, start_date, status, updated, version, assigned_to_id, created_by_id, project_id)
VALUES ('taskId_04', '2016-11-05T11:44:44.797', 'Some description of task 4', '2016-11-05T11:44:44.797', 'task_04',
        '2016-11-05T11:44:44.797', 'PLANNED', '2016-11-05T11:44:44.797', 0, 'uId-2', 'uId-2', 'projectId_01');
INSERT INTO task (id, created, description, end_date, name, start_date, status, updated, version, assigned_to_id, created_by_id, project_id)
VALUES ('taskId_05', '2016-11-05T11:44:44.797', 'Some description of task 5', '2016-11-05T11:44:44.797', 'task_05',
        '2016-11-05T11:44:44.797', 'PAUSED', '2016-11-05T11:44:44.797', 0, 'uId-2', 'uId-2', 'projectId_01');

INSERT INTO comment (id, created, version, commentator_id, task_id, text)
VALUES ('commentId_01', '2016-11-05T11:44:44.797', 0, 'uId-2', 'taskId_01', 'some text 01');
INSERT INTO comment (id, created, version, commentator_id, task_id, text)
VALUES ('commentId_02', '2016-11-05T11:44:44.797', 0, 'uId-1', 'taskId_01', 'some text 02');
INSERT INTO comment (id, created, version, commentator_id, task_id, text)
VALUES ('commentId_03', '2016-11-05T11:44:44.797', 0, 'uId-3', 'taskId_01', 'some text 03');
INSERT INTO comment (id, created, version, commentator_id, task_id, text)
VALUES ('commentId_04', '2016-11-05T11:44:44.797', 0, 'uId-2', 'taskId_01', 'some text 04');
INSERT INTO comment (id, created, version, commentator_id, task_id, text)
VALUES ('commentId_05', '2016-11-05T11:44:44.797', 0, 'uId-1', 'taskId_01', 'some text 05');