package com.innovatech.task;
import com.innovatech.task.model.Task;
import com.innovatech.task.model.TaskStatus;
import org.junit.jupiter.api.Test;
import com.innovatech.task.repository.TaskRepository;
import com.innovatech.task.repository.TaskStatusRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;


import static org.assertj.core.api.Assertions.assertThat;

// AQUÍ ESTÁ LA MAGIA: Forzamos a H2 a levantar y a Hibernate a crear las tablas (create-drop)
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driverClassName=org.h2.Driver",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.show-sql=true"
})
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Test
    void findByDateCreatedBetween_ShouldReturnTasksInDates() {
        // 1. Crear y guardar el Status
        TaskStatus status = new TaskStatus();
        status.setStatus("PENDING");
        status.setTasks(new ArrayList<>());
        TaskStatus savedStatus = taskStatusRepository.saveAndFlush(status);

        // 2. Crear Tarea dentro del rango (2024)
        Task taskNew = new Task();
        taskNew.setName("Task 2024");
        taskNew.setDescription("New");
        taskNew.setDateCreated(LocalDate.of(2024, 5, 1));
        taskNew.setProjectId(1L);
        taskNew.setTaskStatus(savedStatus); // FK
        taskRepository.save(taskNew);

        // 3. Crear Tarea fuera del rango (2023)
        Task taskOld = new Task();
        taskOld.setName("Task 2023");
        taskOld.setDescription("Old");
        taskOld.setDateCreated(LocalDate.of(2023, 1, 1));
        taskOld.setProjectId(1L);
        taskOld.setTaskStatus(savedStatus); // FK
        taskRepository.save(taskOld);
        
        taskRepository.flush();

        // 4. Ejecutar búsqueda
        List<Task> result = taskRepository.findByDateCreatedBetween(
            LocalDate.of(2024, 1, 1), 
            LocalDate.of(2024, 12, 31)
        );

        // 5. Verificar
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Task 2024");
    }
}