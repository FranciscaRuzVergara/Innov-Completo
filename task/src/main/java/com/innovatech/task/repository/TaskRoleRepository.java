package com.innovatech.task.repository;

import com.innovatech.task.model.TaskRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRoleRepository extends JpaRepository<TaskRole, Long> {

    // Para saber todos los roles que tiene asignada una tarea específica
    List<TaskRole> findByTaskIdTask(Long idTask);

    // Para verificar si una combinación específica ya existe (evitar duplicados)
    Optional<TaskRole> findByTaskIdTaskAndRoleId(Long idTask, Long idRole);
}