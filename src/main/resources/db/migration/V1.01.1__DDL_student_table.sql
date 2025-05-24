CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       version INT NOT NULL DEFAULT 0,
                       creation_timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       modification_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255),
                       deleted BOOLEAN NOT NULL DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_role (
                           id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           version INT NOT NULL DEFAULT 0,
                           name VARCHAR(255) NOT NULL,
                           fk_user_id BIGINT NOT NULL,
                           CONSTRAINT fk_user_role_user FOREIGN KEY (fk_user_id) REFERENCES users(id)
                               ON DELETE CASCADE ON UPDATE CASCADE,
                           INDEX idx_user_role_user_id (fk_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE students (
                          id BIGINT NOT NULL PRIMARY KEY,
                          first_name VARCHAR(255) NOT NULL,
                          last_name VARCHAR(255) NOT NULL,
                          student_number VARCHAR(255) NOT NULL UNIQUE,
                          gender VARCHAR(50),
                          CONSTRAINT fk_students_user FOREIGN KEY (id) REFERENCES users(id)
                              ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE password_reset (
                                id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                version INT NOT NULL DEFAULT 0,
                                creation_timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                modification_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                token VARCHAR(255) NOT NULL,
                                expiry_date DATETIME NOT NULL,
                                fk_user_id BIGINT NOT NULL,
                                CONSTRAINT fk_password_reset_user FOREIGN KEY (fk_user_id) REFERENCES users(id)
                                    ON DELETE CASCADE ON UPDATE CASCADE,
                                INDEX idx_password_reset_user_id (fk_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
