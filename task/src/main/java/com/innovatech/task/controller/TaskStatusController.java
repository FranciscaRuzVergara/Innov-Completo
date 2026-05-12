package com.innovatech.task.controller;

import com.innovatech.task.model.TaskStatus;
import com.innovatech.task.service.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task-status")
@CrossOrigin(origins = "*", allowedHeaders = "*") // Cambiar por la ip correspondiente

public class TaskStatusController {

    @Autowired
    private TaskStatusService taskStatusService;

    @GetMapping("/all")
    public List<TaskStatus> getAll() {
        return taskStatusService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        TaskStatus status = taskStatusService.findById(id);
        if (status == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("A task status with this ID does not exist.");
        }
        return ResponseEntity.ok(status);
    }

    @PostMapping("/save")
    public TaskStatus save(@RequestBody TaskStatus taskStatus) {
        return taskStatusService.save(taskStatus);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TaskStatus taskStatus) {
        TaskStatus existingStatus = taskStatusService.findById(id);
    
        if (existingStatus == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Cannot update: A task status with this ID does not exist.");
        }
        taskStatus.setIdTaskStatus(id);
        TaskStatus updatedStatus = taskStatusService.save(taskStatus);
        
        return ResponseEntity.ok(updatedStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        TaskStatus status = taskStatusService.findById(id);
        if (status == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Cannot delete: A task status with this ID does not exist.");
        }
        taskStatusService.deleteTaskStatus(id);
        return ResponseEntity.ok("Task status deleted successfully.");
    }
    
}
