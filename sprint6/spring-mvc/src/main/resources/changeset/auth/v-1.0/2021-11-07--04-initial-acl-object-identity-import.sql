-- liquibase formatted sql
-- changeset 2021-11-07--04-initial-acl-object-identity-import
-- changeset izmalkov-rg:2021-11-07--04-initial-acl-object-identity-import#0001

-- Хранит объекты к которым настраивается доступ
CREATE TABLE IF NOT EXISTS auth.acl_object_identity
(
    id                  bigserial   not null    primary key,
    object_id_class     bigint      not null,
    object_id_identity  bigint      not null,
    parent_object       bigint,
    owner_sid           bigint,
    entries_inheritance bool        not null    default false,

    constraint unique_uk_object_class_identity unique(object_id_class,object_id_identity),

    foreign key (object_id_class)   references auth.acl_class(id),
    foreign key (parent_object)     references auth.acl_object_identity(id),
    foreign key (owner_sid)         references auth.acl_sid(id)
)

--rollback DROP TABLE IF EXISTS auth.acl_object_identity