CREATE SCHEMA client_storage;

CREATE TABLE Client(
    id uuid primary key,
    first_name varchar(15),
    last_name varchar(15),
    patronymic varchar(15),
    mobile_phone varchar(10),
    age int check ( age > 0 ),
    date_registration date default CURRENT_DATE,
    status int default 0,
    passport_id int references Passport(id) unique
);

CREATE TABLE Passport(
    id bigserial primary key,
    serial varchar(5),
    number int,
    gender varchar(3),
    issued_by varchar(15)
);

CREATE TABLE Address(
    id bigserial primary key,
    country varchar(15),
    city varchar(20),
    street varchar(30),
    house int,
    apartment int
);

CREATE TABLE Address_Client(
    client_uuid uuid references Client(id),
    address_id int references Address(id)
);

CREATE SEQUENCE client_uuid_seq;