CREATE SCHEMA client_storage;

CREATE TABLE Client(
    id uuid primary key,
    first_name varchar(15) not null ,
    last_name varchar(15) not null ,
    patronymic varchar(15),
    mobile_phone varchar(10) not null unique,
    age int check ( age > 0 ),
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

