-- liquibase formatted sql
-- changeset 2021-11-07--02-initial-acl-class-import
-- changeset izmalkov-rg:2021-11-07--02-initial-acl-class-import#0001

-- Полное имя класса для организации контроля доступа к объектам этих классов
CREATE TABLE IF NOT EXISTS auth.acl_class
(
    id            bigserial    not null    primary key,
    class         varchar(100) not null,
    class_id_type varchar(100),

    constraint unique_uk_class unique(class)
)

--rollback DROP TABLE IF EXISTS auth.acl_class