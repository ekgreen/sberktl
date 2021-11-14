-- liquibase formatted sql
-- changeset 2021-11-07--03-initial-acl-sid-import
-- changeset izmalkov-rg:2021-11-07--03-initial-acl-sid-import#0001

-- Идентификатор Роли или Пользователя
CREATE TABLE IF NOT EXISTS auth.acl_sid
(
    id        bigserial    primary key,
    sid       varchar(64)  not null,
    principal bool         not null,

    constraint unique_uk_sid_principal unique(sid,principal)
)

--rollback DROP TABLE IF EXISTS auth.acl_sid