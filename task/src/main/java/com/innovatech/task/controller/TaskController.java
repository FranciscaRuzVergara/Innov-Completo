package com.innovatech.task.controller;

import com.innovatech.task.model.Task; // 2. Actualizar import
import com.innovatech.task.service.TaskService; // 2. Actualizar import
import com.innovatech.task.dto.TaskWithProjectDTO; // Importar el DTO para el nuevo endpoint
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks") // Puedes cambiarlo a /api/tasks si quieres estandarizar
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    @GetMapping("/all")
    public List<Task> getAll() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Task task = taskService.findById(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("A task with this ID does not exist.");
        }
        return ResponseEntity.ok(task);
    }

    // 3. NUEVO ENDPOINT: Para usar el método que se conecta al MS de Proyectos
    @GetMapping("/complete/{id}")
    public ResponseEntity<?> getCompleteTaskById(@PathVariable Long id) {
        TaskWithProjectDTO taskComplete = taskService.getTaskComplete(id);
        if (taskComplete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Complete task data not found.");
        }
        return ResponseEntity.ok(taskComplete);
    }

    @GetMapping("/date/{start}/{end}")
    public List<Task> getByDateRange(
            @PathVariable("start") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate start, 
            @PathVariable("end") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate end) {
        return taskService.getTasksByDateRange(start, end);
    }

    @PostMapping("/save")
    public Task save(@RequestBody Task task) {
        return taskService.save(task);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Task task) {
        Task existingTask = taskService.findById(id);
        
        if (existingTask == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("Cannot update: A task with this ID does not exist.");
        }
        task.setIdTask(id); 
        Task updatedTask = taskService.save(task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Task task = taskService.findById(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Cannot delete: A task with this ID does not exist.");
        }
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully.");
    }
}