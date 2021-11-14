-- liquibase formatted sql
-- changeset 2021-11-07--05-initial-acl-entry-import
-- changeset izmalkov-rg:2021-11-07--05-initial-acl-entry-import#0001

-- Настройки прав
CREATE TABLE IF NOT EXISTS auth.acl_entry
(
    id                  bigserial   not null    primary key,
    acl_object_identity bigint      not null,
    ace_order           int         not null,
    sid                 bigint      not null,
    mask                integer     not null,
    granting            bool        not null    default false,
    audit_success       bool        not null,
    audit_false         bool        not null,

    constraint unique_uk_object_identity_ace_order unique(acl_object_identity,ace_order),

    foreign key (acl_object_identity) references auth.acl_object_identity(id),
    foreign key (sid) references auth.acl_sid(id)
)

--rollback DROP TABLE IF EXISTS auth.acl_entry