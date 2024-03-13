CREATE SCHEMA client_storage;

CREATE TABLE Client(
    id uuid primary key,
    first_name varchar(15) not null ,
    last_name varchar(15) not null ,
    patronymic varchar(15),
    mobile_phone varchar(10) not null unique,
    age int check ( age > 0 ),
    email varchar(35) not null unique,
    date_registration date default CURRENT_DATE,
    status int default 0,
    passport_id bigint references Passport(id) unique
);

CREATE TABLE Passport(
    id bigserial primary key,
    serial varchar(5) not null ,
    number varchar(6) not null ,
    gender varchar(3) not null ,
    issued_by varchar(15) not null
);

CREATE TABLE Address(
    id bigserial primary key,
    country varchar(15) not null ,
    city varchar(20) not null ,
    street varchar(30) not null,
    house int not null ,
    apartment int
);

CREATE TABLE Address_Client(
    client_uuid uuid references Client(id),
    address_id int references Address(id)
);

CREATE SEQUENCE client_uuid_seq;

DROP TABLE Address;
DROP TABLE Address_Client;
DROP TABLE Passport;
DROP TABLE Client;

-----------------------

INSERT INTO Client (id, first_name, last_name, patronymic, mobile_phone, age, email, date_registration, status, passport_id)
VALUES
    (gen_random_uuid(), 'Михаил', 'Казан', 'Павлович', '89069438723', 22,'example@mail.ru', current_date, 1, 1),
    (gen_random_uuid(), 'Евгений', 'Толмачев', 'Михалович', '89119931023', 37,'example2@mail.ru', current_date, 1, 2),
    (gen_random_uuid(), 'Анастасия', 'Ломаченко', 'Дмитрьевна', '89992171176', 49,'example3@mail.ru', current_date, 1, 3),
    (gen_random_uuid(), 'Алиса', 'Акопова', 'Владимировна', '89139229100', 19,'example4@mail.ru', current_date, 1, 4),
    (gen_random_uuid(), 'Владимир', 'Ломонов', 'Евгеньевич', '89037102943', 61,'example5@mail.ru', current_date, 0, null),
    (gen_random_uuid(), 'Асуна', 'Кожахметова', 'Тагировна', '89832871080', 18,'example6@mail.ru', current_date, 0, null),
    (gen_random_uuid(), 'Алман', 'Архатавин', null, '8983962284', 29,'example7@mail.ru', current_date, 0, null);

INSERT INTO Passport(serial, number, gender, issued_by)
VALUES
    ('5050','111000','муж','ГУ МВД по Новосибирской обл. Центральный р-он'),
    ('1598','123100','муж','ГУ МВД по Новосибирской обл. Центральный р-он'),
    ('8754','910021','жен','ГУ МВД по Новосибирской обл. Центральный р-он'),
    ('5050','111000','жен','ГУ МВД по Новосибирской обл. Центральный р-он');

INSERT INTO Address(country, city, street, house, apartment)
VALUES
    ('Россия','Новосибирск','Депутатская',43,293),
    ('Россия','Новосибирск','Новогодняя',240,9),
    ('Россия','Новосибирск','Колхозная',1,null);


INSERT INTO Address_Client (client_uuid, address_id)
VALUES
    (uuid_in('b3f62160-c084-41b1-8189-306d1906e2fb'),1),
    (uuid_in('3932fb0d-b735-4018-9292-4b013b5de3ee'),2),
    (uuid_in('0f81f262-2b1e-473a-bdd2-395e46747288'),3),
    (uuid_in('7e0729ed-dc53-4ff7-b710-0ba6ebb65578'),1);


SELECT * FROM Client;
SELECT * FROM Passport;
SELECT * FROM Address;
SELECT * FROM Address_Client;