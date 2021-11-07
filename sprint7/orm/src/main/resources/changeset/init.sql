create schema if not exists reserve;

-- Парк разделен на Зоны
-- PS понятно что место нахождения нужно выносить отдельно, но и так много сущностей
create table if not exists reserve.park(
    id          uuid         constraint park_pk primary key,
    name        varchar(128) not null,
    planet      varchar(32)  not null,
    galaxy      varchar(32)  not null,
    description varchar(512)
);

-- Смотритель парка (егерь) наблюдает за Зонами
create table if not exists reserve.gamekeeper(
    id          uuid         constraint gamekeeper_pk primary key,
    name        varchar(128) not null,
    friendly    varchar(32)  not null
);

-- Зоны разделяют парк на части (например по свойствам обитателей)
-- - За зонами смотрит Егерь
-- - В зонах обитают Животные
-- - У зон есть код, уникальность зоны определяется по связке идентификатора парка и кода зоны
create table if not exists reserve.zone(
    id              uuid         constraint zone_pk primary key,
    code            int          not null,
    name            varchar(128) not null,
    description     varchar(512),

    park_id         uuid         not null,
    gamekeeper_id   uuid         not null,

    unique      (code,park_id),
    foreign key (park_id)       references reserve.park (id),
    foreign key (gamekeeper_id) references reserve.gamekeeper (id)
);

-- Виды чипов
create table if not exists reserve.chip(
    id              uuid         constraint chip_pk primary key,
    brand           varchar(32) not null,
    model           varchar(32) not null,
    validity_period int         not null,
    description     varchar(512),

    unique      (brand,model)
);

-- Животных чипируют для отслеживания их состояния
create table if not exists reserve.chip_instance(
    id                      uuid        constraint chip_instance_pk primary key,
    start_exploitation_date timestamp   not null   default now(),
    is_active               boolean     not null   default true,

    chip_id                 uuid        not null,

    foreign key (chip_id) references reserve.chip (id)
);

-- Животные обитают в Зонах
-- У Животного есть Чип
create table if not exists reserve.animal(
    id                  uuid         constraint animal_pk primary key,
    type                varchar(128) not null,
    name                varchar(128) not null,

    chip_instance_id    uuid         not null,
    zone_id             uuid         not null,

    unique      (chip_instance_id),
    foreign key (chip_instance_id) references reserve.chip_instance (id),
    foreign key (zone_id)          references reserve.zone (id)
);

create index if not exists ix_animal_type on reserve.animal (type);

-- create test entities (ps все клички мужского пола - совпадение)
-- park
insert into reserve.park (id, name, planet, galaxy, description) values ('9e6ee0c6-8f94-4dba-8b28-ae472f4d266b', 'Вымышленный парк', 'Земля', 'Млечный путь', 'Где-то очень далеко, где единороги бегают по лужайкам');
insert into reserve.park (id, name, planet, galaxy, description) values ('aa6e6c50-a796-4e54-8534-992e7b9d057a', 'Парк в другой галактике', 'Пандора', 'Альфа Центавра',  'Межгалактический заповедник, такие животные вам и не снились, а может это даже и не животные!');
-- gamekeeper
insert into reserve.gamekeeper (id, name, friendly) values ('cbbf0d2c-ecc7-4473-9feb-c6ddbf0d8c1b', 'Бешеный Лебовски', 'NOT_FRIENDLY_AT_ALL');
insert into reserve.gamekeeper (id, name, friendly) values ('4c43d4af-b18d-427f-9ce3-a241282ff834', 'Фрейя', 'PEACE_IS_THE_SECOND_NAME');
insert into reserve.gamekeeper (id, name, friendly) values ('c5f7693f-5b9d-469f-a35d-d45d5bc7b71a', 'Просто Ваня', 'NORMAL');
-- zone
insert into reserve.zone (id, code, name, description, park_id, gamekeeper_id)
    values ('50d66d39-d5a4-4298-9958-0fd8ed9a8add', 53, 'Травянистая роща', 'Здесь можно встретить единорога', '9e6ee0c6-8f94-4dba-8b28-ae472f4d266b', '4c43d4af-b18d-427f-9ce3-a241282ff834');
insert into reserve.zone (id, code, name, description, park_id, gamekeeper_id)
    values ('3511e1ba-c733-471b-b3df-04da441f9b64', 21, 'Пустыня "Жаренное солнце"', 'Жарко как в аду, здесь можно встретить только смерть', '9e6ee0c6-8f94-4dba-8b28-ae472f4d266b', 'cbbf0d2c-ecc7-4473-9feb-c6ddbf0d8c1b');
insert into reserve.zone (id, code, name, description, park_id, gamekeeper_id)
    values ('9c78569e-8943-4a3f-8e6c-870925f663e4', 53, 'Равнина динозавров', 'Да-да, вы не ослышались, динозавров!', 'aa6e6c50-a796-4e54-8534-992e7b9d057a', 'c5f7693f-5b9d-469f-a35d-d45d5bc7b71a');
insert into reserve.zone (id, code, name, description, park_id, gamekeeper_id)
    values ('12e75f71-c392-4385-8985-ab4271e4d91a', 17, 'Утес Не Кастерли', 'В скалистой местности бывает опасно, особенно если вы не на Земле', 'aa6e6c50-a796-4e54-8534-992e7b9d057a', 'c5f7693f-5b9d-469f-a35d-d45d5bc7b71a');
insert into reserve.zone (id, code, name, description, park_id, gamekeeper_id)
    values ('dd1d13d7-e1cd-4969-a74c-4fee05ab9e07', 27, 'Водопад Джекели', 'Местность вокруг самого большого водопада, здесь собирается вся живность', 'aa6e6c50-a796-4e54-8534-992e7b9d057a', '4c43d4af-b18d-427f-9ce3-a241282ff834');
-- chip
insert into reserve.chip (id, brand, model, validity_period, description)
    values ('22c01907-368f-41a8-828d-7443b066bd07', 'wall&eva', 'we-213951', 3650, 'Отлично подходил для обитателей Земли и некоторых регионов Пандоры');
insert into reserve.chip (id, brand, model, validity_period, description)
    values ('33171e98-b2a3-4623-9e0c-105863444cf5', 'intergalactic', 'inc-21e5', 2000, 'Подходит крупным обитателям Пандоры');
-- animals
-- Утес Не Кастерли
insert into reserve.chip_instance (id, chip_id)
    values ('9336b8bd-349e-4bff-8b92-4e45b1672ec1', '33171e98-b2a3-4623-9e0c-105863444cf5');
insert into reserve.animal (id, type, name, chip_instance_id, zone_id)
    values ('9336b8bd-349e-4bff-8b92-4e45b1672ec0', 'Арахноид', 'Бэн', '9336b8bd-349e-4bff-8b92-4e45b1672ec1', '12e75f71-c392-4385-8985-ab4271e4d91a');

insert into reserve.chip_instance (id, chip_id)
    values ('ed1e9981-f3f6-46bc-837d-05a3ddbb8c88', '33171e98-b2a3-4623-9e0c-105863444cf5');
insert into reserve.animal (id, type, name, chip_instance_id, zone_id)
    values ('ed1e9981-f3f6-46bc-837d-05a3ddbb8c87', 'Горный банши', 'Фрэд', 'ed1e9981-f3f6-46bc-837d-05a3ddbb8c88', '12e75f71-c392-4385-8985-ab4271e4d91a');

insert into reserve.chip_instance (id, chip_id)
    values ('72cb8868-969d-473a-b3a5-860ec745ba6e', '33171e98-b2a3-4623-9e0c-105863444cf5');
insert into reserve.animal (id, type, name, chip_instance_id, zone_id)
    values ('72cb8868-969d-473a-b3a5-860ec745ba5e', 'Горный банши', 'Фрэнд', '72cb8868-969d-473a-b3a5-860ec745ba6e', '12e75f71-c392-4385-8985-ab4271e4d91a');

insert into reserve.chip_instance (id, chip_id)
    values ('2bbcecce-0c6d-4578-b0be-b356f97fcac5', '33171e98-b2a3-4623-9e0c-105863444cf5');
insert into reserve.animal (id, type, name, chip_instance_id, zone_id)
    values ('2bbcecce-0c6d-4578-b0be-b356f97fcac4', 'Кентрокапра', 'Креветка', '2bbcecce-0c6d-4578-b0be-b356f97fcac5', '12e75f71-c392-4385-8985-ab4271e4d91a');

-- Равнина динозавров
insert into reserve.chip_instance (id, chip_id)
    values ('ec79413f-fd27-4856-81ca-acae12dd65c9', '22c01907-368f-41a8-828d-7443b066bd07');
insert into reserve.animal (id, type, name, chip_instance_id, zone_id)
    values ('ec79413f-fd27-4856-81ca-acae12dd65c8', 'Молотоглавый титанотерий', 'Терентий', 'ec79413f-fd27-4856-81ca-acae12dd65c9', '9c78569e-8943-4a3f-8e6c-870925f663e4');

insert into reserve.chip_instance (id, chip_id)
    values ('bb8fc9d7-2fff-4990-b225-dee49c4480c0', '22c01907-368f-41a8-828d-7443b066bd07');
insert into reserve.animal (id, type, name, chip_instance_id, zone_id)
    values ('bb8fc9d7-2fff-4990-b225-dee49c4480c9', 'Трицерапторс', 'Арсений', 'bb8fc9d7-2fff-4990-b225-dee49c4480c0', '9c78569e-8943-4a3f-8e6c-870925f663e4');

insert into reserve.chip_instance (id, chip_id)
    values ('d07c376d-75f4-4984-a1ed-866569c74018', '33171e98-b2a3-4623-9e0c-105863444cf5');
insert into reserve.animal (id, type, name, chip_instance_id, zone_id)
    values ('d07c376d-75f4-4984-a1ed-866569c74017', 'Диплодок', 'Жираф', 'd07c376d-75f4-4984-a1ed-866569c74018', '9c78569e-8943-4a3f-8e6c-870925f663e4');

-- Водопад Джекели
insert into reserve.chip_instance (id, chip_id)
    values ('9a3729ab-d812-4e46-a380-c85dcaf1c1e1', '22c01907-368f-41a8-828d-7443b066bd07');
insert into reserve.animal (id, type, name, chip_instance_id, zone_id)
    values ('9a3729ab-d812-4e46-a380-c85dcaf1c1e0', 'Анемоноид', 'Сирена', '9a3729ab-d812-4e46-a380-c85dcaf1c1e1', 'dd1d13d7-e1cd-4969-a74c-4fee05ab9e07');

insert into reserve.chip_instance (id, chip_id)
    values ('1a9ee854-a4d0-4216-a87a-1fb5a663bcb4', '22c01907-368f-41a8-828d-7443b066bd07');
insert into reserve.animal (id, type, name, chip_instance_id, zone_id)
    values ('1a9ee854-a4d0-4216-a87a-1fb5a663bcb3', 'Стурмбист', 'Бист', '1a9ee854-a4d0-4216-a87a-1fb5a663bcb4', 'dd1d13d7-e1cd-4969-a74c-4fee05ab9e07');

insert into reserve.chip_instance (id, chip_id)
    values ('76bdff01-2b8a-47b7-8464-648447a884c8', '22c01907-368f-41a8-828d-7443b066bd07');
insert into reserve.animal (id, type, name, chip_instance_id, zone_id)
    values ('76bdff01-2b8a-47b7-8464-648447a884c7', 'Тетраптерон', 'Чайка', '76bdff01-2b8a-47b7-8464-648447a884c8', 'dd1d13d7-e1cd-4969-a74c-4fee05ab9e07');

-- Травянистая роща
insert into reserve.chip_instance (id, chip_id)
    values ('ba567ffa-98d5-42e1-9d5b-cb042bc9da30', '22c01907-368f-41a8-828d-7443b066bd07');
insert into reserve.animal (id, type, name, chip_instance_id, zone_id)
    values ('ba567ffa-98d5-42e1-9d5b-cb042bc9da39', 'Единорог', 'Искорка Искорка', 'ba567ffa-98d5-42e1-9d5b-cb042bc9da30', '50d66d39-d5a4-4298-9958-0fd8ed9a8add');

insert into reserve.chip_instance (id, chip_id)
    values ('bcb0a765-ab62-48d5-9832-58d701588540', '22c01907-368f-41a8-828d-7443b066bd07');
insert into reserve.animal (id, type, name, chip_instance_id, zone_id)
    values ('bcb0a765-ab62-48d5-9832-58d701588549', 'Единорог', 'Пинки Пай', 'bcb0a765-ab62-48d5-9832-58d701588540', '50d66d39-d5a4-4298-9958-0fd8ed9a8add');

insert into reserve.chip_instance (id, chip_id)
    values ('3b7075e7-10f3-4f8f-b758-134c0d1fd0d9', '22c01907-368f-41a8-828d-7443b066bd07');
insert into reserve.animal (id, type, name, chip_instance_id, zone_id)
    values ('3b7075e7-10f3-4f8f-b758-134c0d1fd0d8', 'Единорог', 'Принцесса Селестия', '3b7075e7-10f3-4f8f-b758-134c0d1fd0d9', '50d66d39-d5a4-4298-9958-0fd8ed9a8add');


-- Пустыня "Жаренное солнце"
insert into reserve.chip_instance (id, chip_id)
    values ('3b7075e7-10f3-4f8f-b758-134c0d1fd0d7', '22c01907-368f-41a8-828d-7443b066bd07');
insert into reserve.animal (id, type, name, chip_instance_id, zone_id)
    values ('3b7075e7-10f3-4f8f-b758-134c0d1fd0d6', 'Шакал', 'Бывший', '3b7075e7-10f3-4f8f-b758-134c0d1fd0d7', '3511e1ba-c733-471b-b3df-04da441f9b64');

insert into reserve.chip_instance (id, chip_id)
    values ('264e50c7-3427-49e1-a304-b56d4e1470d5', '22c01907-368f-41a8-828d-7443b066bd07');
insert into reserve.animal (id, type, name, chip_instance_id, zone_id)
    values ('264e50c7-3427-49e1-a304-b56d4e1470d4', 'Барханные коты', 'Бархан', '264e50c7-3427-49e1-a304-b56d4e1470d5', '3511e1ba-c733-471b-b3df-04da441f9b64');

insert into reserve.chip_instance (id, chip_id)
    values ('8b92f4b7-30ce-491e-bf38-5b8ea35c4686', '22c01907-368f-41a8-828d-7443b066bd07');
insert into reserve.animal (id, type, name, chip_instance_id, zone_id)
    values ('8b92f4b7-30ce-491e-bf38-5b8ea35c4685', 'Агам', 'Мага', '8b92f4b7-30ce-491e-bf38-5b8ea35c4686', '3511e1ba-c733-471b-b3df-04da441f9b64');