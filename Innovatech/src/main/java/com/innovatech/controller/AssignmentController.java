package com.innovatech.controller;

import com.innovatech.model.Assignment;
import com.innovatech.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignments")
@CrossOrigin("*")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @GetMapping
    public List<Assignment> getAll() {
        return assignmentService.getAll();
    }

    @GetMapping("/find/{rut}/{taskId}")
    public ResponseEntity<?> getSpecific(@PathVariable String rut, @PathVariable Long taskId) {
        Assignment assignment = assignmentService.getSpecificAssignment(rut, taskId);
        if (assignment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró la asignación para el RUT: " + rut + " y Tarea ID: " + taskId);
        }
        return ResponseEntity.ok(assignment);
    }

    @PostMapping
    public ResponseEntity<Assignment> save(@RequestBody Assignment assignment) {
    return ResponseEntity.ok(assignmentService.save(assignment));
}

    @DeleteMapping("/delete/{rut}/{taskId}")
    public ResponseEntity<?> delete(@PathVariable String rut, @PathVariable Long taskId) {
        Assignment existing = assignmentService.getSpecificAssignment(rut, taskId);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se pudo eliminar: La asignación no existe.");
        }
        assignmentService.delete(rut, taskId);
        return ResponseEntity.ok("Asignación eliminada correctamente.");
    }
}
