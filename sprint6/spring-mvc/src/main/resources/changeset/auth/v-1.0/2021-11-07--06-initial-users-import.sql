-- liquibase formatted sql
-- changeset 2021-11-07--06-initial-users-import
-- changeset izmalkov-rg:2021-11-07--06-initial-users-import#0001

-- Настройки прав
CREATE TABLE IF NOT EXISTS auth.users
(
    username varchar(50)  not null primary key,
    password varchar(500) not null,
    email    varchar(100) not null,
    enabled  boolean      not null default true
);

--rollback DROP TABLE IF EXISTS auth.users