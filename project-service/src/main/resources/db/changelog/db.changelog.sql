--liquibase formatted sql

--changeset innovatech:1
-- Creación de la tabla de estados
CREATE TABLE status (
    project_status_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    status_name VARCHAR(255) NOT NULL
);

--changeset innovatech:2
-- Creación de la tabla de proyectos
CREATE TABLE project (
    project_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    project_status_id BIGINT NOT NULL,
    CONSTRAINT fk_project_status FOREIGN KEY (project_status_id) REFERENCES status(project_status_id)
);

--changeset innovatech:3
-- Creación de la tabla de KPIs
CREATE TABLE kpi (
    kpi_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    percentage INT,
    registration_date DATE NOT NULL,
    project_id BIGINT NOT NULL,
    CONSTRAINT fk_kpi_project FOREIGN KEY (project_id) REFERENCES project(project_id)
);

--changeset innovatech:4
-- Datos iniciales para estados
INSERT INTO status (status_name) VALUES ('PLANNING'), ('IN_PROGRESS'), ('COMPLETED');

--changeset innovatech:5
-- Datos iniciales para proyectos (ID 1 usará el estado PLANNING que es ID 1)
INSERT INTO project (name, description, start_date, project_status_id) 
VALUES ('Sistema de Gestión Innova', 'Desarrollo de microservicios core', '2024-01-15', 1);

--changeset innovatech:6
-- Datos iniciales para KPIs (Relacionados al proyecto ID 1)
INSERT INTO kpi (name, percentage, registration_date, project_id) 
VALUES ('Avance de Backend', 45, '2024-05-20', 1);