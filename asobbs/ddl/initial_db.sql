CREATE DATABASE asobbs;
CREATE DATABASE asobbs_st;
CREATE USER 'asobbs_user'@'localhost' IDENTIFIED BY 'AsoBBS-User123#';
GRANT ALL PRIVILEGES ON asobbs.* TO 'asobbs_user'@'localhost';
GRANT ALL PRIVILEGES ON asobbs_st.* TO 'asobbs_user'@'localhost';
