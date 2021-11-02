-- liquibase formatted sql
-- changeset 2021-11-02--03-create-account-index
-- changeset izmalkov-rg:2021-11-02--03-create-account-index#0001

CREATE UNIQUE INDEX IF NOT EXISTS account_id_index ON rdbms.account (id)

--rollback DROP INDEX IF EXISTS rdbms.account_id_index