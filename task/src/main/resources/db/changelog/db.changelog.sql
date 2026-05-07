--liquibase formatted sql

--changeset fran:1
CREATE TABLE task_status (
    id_task_status BIGINT AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(255) NOT NULL
);

--changeset fran:2
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name_role VARCHAR(100) NOT NULL
);

--changeset fran:3
CREATE TABLE tasks (
    id_task BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    date_created DATE NOT NULL,
    date_finished DATE,
    project_id BIGINT,
    task_status_id BIGINT NOT NULL,
    CONSTRAINT fk_task_status FOREIGN KEY (task_status_id) REFERENCES task_status(id_task_status)
);

--changeset fran:4
CREATE TABLE task_roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_task_role_task FOREIGN KEY (task_id) REFERENCES tasks(id_task),
    CONSTRAINT fk_task_role_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

--changeset fran:5
INSERT INTO task_status (status) VALUES 
('Por Hacer'), 
('En Progreso'), 
('En Revisión'), 
('Completada');

--changeset fran:6
INSERT INTO roles (name_role) VALUES 
('Desarrollador Frontend'), 
('Desarrollador Backend'), 
('Arquitecto Cloud / DevOps'), 
('QA Automator'), 
('Diseñador UI/UX');

--changeset fran:7
INSERT INTO tasks (name, description, date_created, date_finished, project_id, task_status_id) VALUES
('Crear UI del Login', 'Maquetar la pantalla de login con React y Vite', '2026-05-01', NULL, 1, 2),
('Implementacion JWT', 'Implementacion de JWT en microservicios', '2026-04-28', '2026-05-02', 1, 4),
('Configurar VPC', 'Desplegar subredes públicas y privadas, y NAT Gateways', '2026-05-04', NULL, 2, 1);

--changeset fran:8
INSERT INTO task_roles (task_id, role_id) VALUES
(1, 1), 
(1, 5), 
(2, 2), 
(3, 3); 