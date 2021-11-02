-- liquibase formatted sql
-- changeset 2021-11-02--04-dml-create-user-accounts
-- changeset izmalkov-rg:2021-11-02--04-dml-create-user-accounts#0001

DO
'
DECLARE
    t_count INTEGER := 0;
BEGIN
    SELECT count(to_regclass(''rdbms.account'')) into t_count;
        IF t_count = 1 THEN
            -- добавим два тестовых счета
            INSERT INTO rdbms.account (amount) VALUES (2000);
            INSERT INTO rdbms.account (amount) VALUES (2000);
        END IF;
    END;
';