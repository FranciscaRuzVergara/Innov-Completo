--liquibase formatted sql

--changeset brayan:1
CREATE TABLE employee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rut VARCHAR(20) NOT NULL UNIQUE,
    employee_name VARCHAR(100) NOT NULL,
    employee_surname VARCHAR(100) NOT NULL,
    employee_mail VARCHAR(150) NOT NULL UNIQUE,
    total_hours INT,
    dv VARCHAR(1)
);

--changeset brayan:2
INSERT INTO employee (id, rut, employee_name, employee_surname, employee_mail, total_hours, dv) VALUES
(1, '1111111-1','Juan', 'Perez', 'juanperez@gmail.com', 400, 'A'),
(2, '0000000-0', 'Pedro', 'pancho', 'pedrop@gmail.com', 1300, 'A');