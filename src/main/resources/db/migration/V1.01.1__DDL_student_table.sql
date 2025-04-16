CREATE TABLE users (
                       id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       version int DEFAULT 0,
                       creation_timestamp datetime,
                       modification_timestamp datetime,
                       email varchar(255) NOT NULL,
                       password varchar(255),
                       deleted boolean DEFAULT FALSE
);

CREATE TABLE user_role (
                           id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           version int DEFAULT 0,
                           name varchar(255) NOT NULL,
                           fk_user_id bigint,
                           CONSTRAINT fk_user FOREIGN KEY (fk_user_id) REFERENCES users(id)
);

CREATE TABLE students (
                          id bigint NOT NULL PRIMARY KEY,
                          first_name varchar(255),
                          last_name varchar(255),
                          student_number varchar(255),
                          gender varchar(255),
                          CONSTRAINT fk_students_user FOREIGN KEY (id) REFERENCES users(id)
);