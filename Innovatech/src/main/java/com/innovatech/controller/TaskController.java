package com.innovatech.controller;

import com.innovatech.model.Task;
import com.innovatech.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*", allowedHeaders = "*") // Cambiar por la ip correspondiente

public class TaskController {
    
    @Autowired
    private TaskService taskService;

    @GetMapping
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

    @GetMapping("/date/{start}/{end}")
    public List<Task> getByDateRange(
            @PathVariable("start") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate start, 
            @PathVariable("end") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate end) {
        return taskService.getTasksByDateRange(start, end);
    }

    @PostMapping
    public Task save(@RequestBody Task task) {
        return taskService.save(task);
    }

    @PutMapping("/{id}")
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

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Task>> getTasksByProject(@PathVariable Long projectId){
        List<Task> tasks = taskService.getTasksByProjectId(projectId);
        return ResponseEntity.ok(tasks);
    }
    
}
