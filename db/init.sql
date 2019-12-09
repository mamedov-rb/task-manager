CREATE USER task_admin;
ALTER USER task_admin WITH PASSWORD '123qwe';
ALTER USER task_admin CREATEDB;
CREATE DATABASE task_db OWNER task_admin;

