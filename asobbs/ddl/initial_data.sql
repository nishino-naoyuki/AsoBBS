CREATE DATABASE asobbs;
CREATE USER 'asobbs_user'@'localhost' IDENTIFIED BY 'AsoBBS-User123#';
GRANT ALL PRIVILEGES ON asobbs.* TO 'asobbs_user'@'localhost';
