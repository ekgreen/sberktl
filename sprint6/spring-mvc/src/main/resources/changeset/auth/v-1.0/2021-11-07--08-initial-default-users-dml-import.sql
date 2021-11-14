-- liquibase formatted sql
-- changeset 2021-11-07--08-initial-default-users-dml-import
-- changeset izmalkov-rg:2021-11-07--08-initial-default-users-dml-import#0001

DO
'
DECLARE
    t_count INTEGER := 0;
BEGIN
    SELECT count(to_regclass(''auth.users'')) into t_count;
    IF t_count = 1 THEN
        -- добавим два тестовых счета
        INSERT INTO auth.users (username, password, email) VALUES (''Wall-E'', ''$2a$10$qon5NeK.VGGUlQTOek5sBec1q21xMfD9ct76OZz0jZ6VqmpTXE94.'', ''walle@space.in'');
        INSERT INTO auth.users (username, password, email) VALUES (''Eva'', ''$2a$10$gCRR/oAzkdjC0Bi3NukyxOgmK7zggY/tRWgoTT1wTPEZ7dvgTXrL2'', ''eva@space.in'');
        INSERT INTO auth.users (username, password, email) VALUES (''Alien'', ''$2a$10$ie9zjdUDHxo9IqVaFXfzyudbCkT/Mx9jwI9ZZPlWebKvRXHV/n.9K'', ''alien@space.in'');
    END IF;

    SELECT count(to_regclass(''auth.authorities'')) into t_count;
    IF t_count = 1 THEN
        -- добавим два тестовых счета
        INSERT INTO auth.authorities (username, authority) VALUES (''Wall-E'', ''ROLE_ADMIN'');
        INSERT INTO auth.authorities (username, authority) VALUES (''Eva'', ''ROLE_APP_OWNER'');
        END IF;
END;
';
