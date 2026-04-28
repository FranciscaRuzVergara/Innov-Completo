package com.innovatech.task.service;

import com.innovatech.task.model.Role;
import com.innovatech.task.model.Task;
import com.innovatech.task.model.TaskRole;
import com.innovatech.task.repository.RoleRepository;
import com.innovatech.task.repository.TaskRepository;
import com.innovatech.task.repository.TaskRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskRoleService {

    private final TaskRoleRepository taskRoleRepository;
    private final TaskRepository taskRepository;
    private final RoleRepository roleRepository;

    // Obtener los roles que requiere una tarea específica
    @Transactional(readOnly = true)
    public List<TaskRole> getRolesByTaskId(Long taskId) {
        return taskRoleRepository.findByTaskIdTask(taskId);
    }

    // VINCULAR UNA TAREA CON UN ROL (¡Este es el método clave!)
    @Transactional
    public TaskRole assignRoleToTask(Long taskId, Long roleId) {
        // 1. Validar que la combinación no exista ya
        taskRoleRepository.findByTaskIdTaskAndRoleId(taskId, roleId)
                .ifPresent(tr -> {
                    throw new RuntimeException("La tarea ya tiene este rol asignado.");
                });

        // 2. Buscar las entidades
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // 3. Crear y guardar la relación
        TaskRole taskRole = new TaskRole();
        taskRole.setTask(task);
        taskRole.setRole(role);

        return taskRoleRepository.save(taskRole);
    }
    
    @Transactional
    public void removeRoleFromTask(Long taskRoleId) {
        taskRoleRepository.deleteById(taskRoleId);
    }

    @Transactional(readOnly = true)
    public TaskRole findById(Long id) {
    return taskRoleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Relación Rol-Tarea no encontrada con ID: " + id));
    }
}