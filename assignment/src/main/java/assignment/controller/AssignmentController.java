package assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import assignment.model.Assignment;
import assignment.service.AssignmentService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<Assignment> createAssignment(@RequestBody Assignment assignment) {

        Assignment newAssignment = assignmentService.createAssignment(assignment);
        
        return new ResponseEntity<>(newAssignment, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        List<Assignment> assignments = assignmentService.getAllAssignments();
        
        if (assignments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<>(assignments, HttpStatus.OK);
    }
}
