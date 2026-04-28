package com.innovatech.task.controller;

import com.innovatech.task.model.TaskRole;
import com.innovatech.task.service.TaskRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-roles")
@RequiredArgsConstructor
public class TaskRoleController {

    private final TaskRoleService taskRoleService;

    // Obtener todas las combinaciones de roles de una tarea
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskRole>> getRolesByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskRoleService.getRolesByTaskId(taskId));
    }

    // Vincular una tarea con un rol (Ej: Tarea 5 ahora requiere un "Backend Developer")
    @PostMapping("/assign")
    public ResponseEntity<TaskRole> assignRoleToTask(@RequestParam Long taskId, @RequestParam Long roleId) {
        return ResponseEntity.ok(taskRoleService.assignRoleToTask(taskId, roleId));
    }

    // Endpoint para que el MS Assignment verifique si un ID de TaskRole existe
    @GetMapping("/{id}")
    public ResponseEntity<TaskRole> getById(@PathVariable Long id) {
        try {
            TaskRole taskRole = taskRoleService.findById(id);
            return ResponseEntity.ok(taskRole);
        } catch (RuntimeException e) {
            // Si el service lanza la excepción, el controller responde 404
            return ResponseEntity.notFound().build();
        }
    }
}