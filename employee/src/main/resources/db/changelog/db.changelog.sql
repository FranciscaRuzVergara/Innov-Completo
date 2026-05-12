--liquibase formatted sql

--changeset brayan:1
CREATE TABLE employee (
    rut VARCHAR(20) PRIMARY KEY,
    employee_name VARCHAR(100) NOT NULL,
    employee_surname VARCHAR(100) NOT NULL,
    employee_mail VARCHAR(150) NOT NULL UNIQUE,
    total_hours INT,
    dv VARCHAR(1)
);

--changeset brayan:2
INSERT INTO employee (rut, employee_name, employee_surname, employee_mail, total_hours, dv) VALUES
('1111111-1','Juan', 'Perez', 'juanperez@gmail.com', 400, 'A'),
('0000000-0', 'Pedro', 'pancho', 'pedrop@gmail.com', 1300, 'A');