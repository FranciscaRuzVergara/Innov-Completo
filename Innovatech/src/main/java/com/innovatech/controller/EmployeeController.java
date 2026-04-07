package com.innovatech.controller;


import com.innovatech.dto.EmployeeRequestDTO;
import com.innovatech.dto.EmployeeResponseDTO;
import com.innovatech.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{rut}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeByRut(@PathVariable String rut) {
        return employeeService.getEmployeeByRut(rut)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeByEmail(@PathVariable String email) {
        return employeeService.getEmployeeByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByRole(@PathVariable Long roleId) {
        return ResponseEntity.ok(employeeService.getEmployeesByRole(roleId));
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@RequestBody EmployeeRequestDTO dto) {
        try {
            EmployeeResponseDTO created = employeeService.createEmployee(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{rut}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable String rut, @RequestBody EmployeeRequestDTO dto) 
    {
        try {
            EmployeeResponseDTO updated = employeeService.updateEmployee(rut, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable String rut) {
        try {
            employeeService.deleteEmployee(rut);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
