-- init.sql

CREATE DATABASE IF NOT EXISTS hms;

CREATE USER IF NOT EXISTS 'hms_user'@'%' IDENTIFIED BY 'hms_password';

GRANT CREATE, SELECT, INSERT, UPDATE, DELETE ON hms.* TO 'hms_user'@'%';
