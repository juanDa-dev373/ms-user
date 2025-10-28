CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       firstname VARCHAR(255),
                       lastname VARCHAR(255),
                       country VARCHAR(255),
                       password VARCHAR(255) NOT NULL,
                       role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER',
                       account_non_expired BIT DEFAULT b'1',
                       account_non_locked BIT DEFAULT b'1',
                       credentials_non_expired BIT DEFAULT b'1',
                       enabled BIT DEFAULT b'1',
                       PRIMARY KEY (id)
);

CREATE TABLE password_reset_token (
                                      id BIGINT NOT NULL AUTO_INCREMENT,
                                      token VARCHAR(64) NOT NULL UNIQUE,
                                      user_id BIGINT NOT NULL,
                                      expires_at DATETIME(6) NOT NULL,
                                      used BIT DEFAULT b'0',
                                      PRIMARY KEY (id)
);