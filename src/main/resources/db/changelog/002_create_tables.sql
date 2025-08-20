--liquibase formatted sql
-- changeset table:001

CREATE TABLE curator (
                         id SERIAL PRIMARY KEY,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         surname VARCHAR(255) NOT NULL,
                         name VARCHAR(255) NOT NULL,
                         patronymic VARCHAR(255),
                         phone VARCHAR(50) NOT NULL,
                         deleted_at TIMESTAMP,
                         role roles NOT NULL
);

CREATE TABLE "user" (
                        id SERIAL PRIMARY KEY,
                        user_vk_id INTEGER,
                        user_telegram_id VARCHAR(255),
                        name VARCHAR(255),
                        surname VARCHAR(255),
                        patronymic VARCHAR(255),
                        email VARCHAR(255) NOT NULL UNIQUE,
                        organization_name VARCHAR(255),
                        inn VARCHAR(50) NOT NULL,
                        phone VARCHAR(50) NOT NULL,
                        code VARCHAR(255) NOT NULL UNIQUE,
                        status status NOT NULL,
                        deleted_at TIMESTAMP,
                        curator_id INTEGER NOT NULL,
                        category categories NOT NULL,
                        okved VARCHAR(255),
                        updated_at TIMESTAMP
);

CREATE TABLE request (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255),
                         surname VARCHAR(255),
                         patronymic VARCHAR(255),
                         email VARCHAR(255) NOT NULL,
                         organization_name VARCHAR(255),
                         inn VARCHAR(50) NOT NULL,
                         phone VARCHAR(50) NOT NULL,
                         category categories NOT NULL,
                         label labels[] NOT NULL,
                         created_at TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE event (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description TEXT NOT NULL,
                       date_start TIMESTAMP NOT NULL,
                       date_end TIMESTAMP NOT NULL,
                       deleted_at TIMESTAMP,
                       updated_at TIMESTAMP,
                       labels labels[] NOT NULL DEFAULT '{}',
                       okved TEXT[] NOT NULL DEFAULT '{}',
                       curator_id INTEGER,
                       created_at TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE user_on_event (
                               user_id INTEGER NOT NULL,
                               event_id INTEGER NOT NULL,
                               status event_status NOT NULL,
                               reaction reaction,
                               date_reaction TIMESTAMP,
                               date_status TIMESTAMP,
                               PRIMARY KEY (user_id, event_id)
);

CREATE TABLE feedback (
                          id SERIAL PRIMARY KEY,
                          created_at TIMESTAMP DEFAULT now() NOT NULL,
                          status feedback_status NOT NULL,
                          user_id INTEGER NOT NULL,
                          reaction reaction,
                          date_reaction TIMESTAMP
);

CREATE TABLE comment (
                         id SERIAL PRIMARY KEY,
                         value TEXT NOT NULL,
                         feedback_id INTEGER NOT NULL,
                         created_at TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE messages (
                          id SERIAL PRIMARY KEY,
                          curator_id INTEGER NOT NULL,
                          user_id INTEGER NOT NULL,
                          text TEXT NOT NULL,
                          created_at TIMESTAMP DEFAULT now() NOT NULL
);
