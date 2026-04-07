package com.innovatech.service;

import com.innovatech.model.Assignment;
import com.innovatech.model.Employee;
import com.innovatech.model.Task;
import com.innovatech.repository.AssignmentRepository;
import com.innovatech.repository.EmployeeRepository;
import com.innovatech.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private TaskRepository taskRepository;

    public Assignment getSpecificAssignment(String rut, Long taskId) {
        System.out.println("Buscando asignación para RUT: " + rut + " y Task: " + taskId);
        return assignmentRepository.findByIdEmployeeRutAndIdTaskId(rut, taskId)
                                   .orElse(null);
    }
    
    public List<Assignment> getAll() {
        return assignmentRepository.findAll();
    }

    public Assignment save(Assignment assignment) {
        String rut = assignment.getId().getEmployeeRut();
        Long taskId = assignment.getId().getTaskId();

        Employee emp = employeeRepository.findById(rut)
            .orElseThrow(() -> new RuntimeException("Employee don't found"));
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task don't found"));

        assignment.setEmployee(emp);
        assignment.setTask(task);

        return assignmentRepository.save(assignment);
    }


    public void delete(String rut, Long taskId) {
        Assignment existing = getSpecificAssignment(rut, taskId);
        if (existing != null) {
            assignmentRepository.delete(existing);
        }
    }
}
