CREATE USER task_manager_admin;
ALTER USER task_manager_admin WITH PASSWORD '123qwe';
ALTER USER task_manager_admin CREATEDB;
CREATE DATABASE task_manager_db OWNER task_manager_admin;

