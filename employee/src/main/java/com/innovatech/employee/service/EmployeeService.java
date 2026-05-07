package com.innovatech.employee.service;

import com.innovatech.employee.dto.EmployeeRequestDTO;
import com.innovatech.employee.dto.EmployeeResponseDTO;
import com.innovatech.employee.model.Employee;
import com.innovatech.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    
    // ❌ ELIMINADO: private final RoleRepository roleRepository;

    // Mapper: Employee -> EmployeeResponseDTO
    private EmployeeResponseDTO toResponseDTO(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getRut(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getTotalHours(),
                employee.getDv()
               
        );
    }

    // Mapper: EmployeeRequestDTO -> Employee
    private Employee toEntity(EmployeeRequestDTO dto) {
        Employee employee = new Employee();
        employee.setRut(dto.getRut());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setTotalHours(dto.getTotalHours());
        employee.setDv(dto.getDv());
        return employee;
    }

    // Get all employees
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Get employee by RUT
    public Optional<EmployeeResponseDTO> getEmployeeByRut(String rut) {
        return employeeRepository.findById(rut)
                .map(this::toResponseDTO);
    }

    // Get employee by email
    public Optional<EmployeeResponseDTO> getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .map(this::toResponseDTO);
    }

   
    // Create employee
    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered: " + dto.getEmail());
        }
        return toResponseDTO(employeeRepository.save(toEntity(dto)));
    }

    // Update employee
    @Transactional
    public EmployeeResponseDTO updateEmployee(String rut, EmployeeRequestDTO dto) {
        Employee existing = employeeRepository.findById(rut)
                .orElseThrow(() -> new RuntimeException("Employee not found with RUT: " + rut));

       

        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        existing.setTotalHours(dto.getTotalHours());
        existing.setDv(dto.getDv());
  

        return toResponseDTO(employeeRepository.save(existing));
    }

    // Delete employee
    @Transactional
    public void deleteEmployee(String rut) {
        if (!employeeRepository.existsById(rut)) {
            throw new RuntimeException("Employee not found with RUT: " + rut);
        }
        employeeRepository.deleteById(rut);
    }
}