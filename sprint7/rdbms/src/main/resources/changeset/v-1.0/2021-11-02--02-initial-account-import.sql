-- liquibase formatted sql
-- changeset 2021-11-02--02-initial-schema-import
-- changeset izmalkov-rg:2021-11-02--02-initial-schema-import#0001

CREATE TABLE IF NOT EXISTS rdbms.account
(
    id      bigserial constraint account_pk primary key,
    amount  int       check ( amount >= 0 ),
    version int       default 0
)

--rollback DROP TABLE IF EXISTS rdbms.account