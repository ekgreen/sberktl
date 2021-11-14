-- liquibase formatted sql
-- changeset 2021-11-07--07-initial-authorities-import
-- changeset izmalkov-rg:2021-11-07--07-initial-authorities-import#0001

-- Настройки прав
CREATE TABLE IF NOT EXISTS auth.authorities
(
    id        bigserial   primary key,
    username  varchar(50) not null,
    authority varchar(50) not null,

    unique (username,authority),
    constraint fk_authorities_users foreign key(username) references auth.users(username)
);

--rollback DROP TABLE IF EXISTS auth.authorities