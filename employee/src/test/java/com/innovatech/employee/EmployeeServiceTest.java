package com.innovatech.employee;

import com.innovatech.employee.dto.EmployeeRequestDTO;
import com.innovatech.employee.dto.EmployeeResponseDTO;
import com.innovatech.employee.model.Employee;
import com.innovatech.employee.repository.EmployeeRepository;
import com.innovatech.employee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
 
import java.util.List;
import java.util.Optional;
 
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
 
    @Mock
    private EmployeeRepository employeeRepository;
 
    @InjectMocks
    private EmployeeService employeeService;
 
    private Employee employee;
    private EmployeeRequestDTO requestDTO;
 
    @BeforeEach
    void setUp() {
        employee = new Employee("12345678", "Juan", "Pérez", "juan@example.com", 40, "9");
        requestDTO = new EmployeeRequestDTO("12345678", "Juan", "Pérez", "juan@example.com", 40, "9");
    }
 
    // ✅ Test 1: createEmployee guarda y retorna el DTO correctamente
    @Test
    void createEmployee_shouldReturnResponseDTO_whenEmailNotRegistered() {
        when(employeeRepository.existsByEmail(requestDTO.getEmail())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
 
        EmployeeResponseDTO result = employeeService.createEmployee(requestDTO);
 
        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo("12345678");
        assertThat(result.getEmail()).isEqualTo("juan@example.com");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }
 
    // ✅ Test 2: createEmployee lanza excepción si el email ya existe
    @Test
    void createEmployee_shouldThrowException_whenEmailAlreadyRegistered() {
        when(employeeRepository.existsByEmail(requestDTO.getEmail())).thenReturn(true);
 
        assertThatThrownBy(() -> employeeService.createEmployee(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already registered");
 
        verify(employeeRepository, never()).save(any());
    }
 
    // ✅ Test 3: deleteEmployee lanza excepción si el RUT no existe
    @Test
    void deleteEmployee_shouldThrowException_whenRutNotFound() {
        when(employeeRepository.existsById("99999999")).thenReturn(false);
 
        assertThatThrownBy(() -> employeeService.deleteEmployee("99999999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Employee not found with RUT: 99999999");
 
        verify(employeeRepository, never()).deleteById(any());
    }
 
    // ✅ Bonus: getEmployeeByRut retorna vacío si no existe
    @Test
    void getEmployeeByRut_shouldReturnEmpty_whenNotFound() {
        when(employeeRepository.findById("00000000")).thenReturn(Optional.empty());
 
        Optional<EmployeeResponseDTO> result = employeeService.getEmployeeByRut("00000000");
 
        assertThat(result).isEmpty();
    }
 
    // ✅ Bonus: getAllEmployees retorna lista con todos los empleados
    @Test
    void getAllEmployees_shouldReturnListOfEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));
 
        List<EmployeeResponseDTO> result = employeeService.getAllEmployees();
 
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRut()).isEqualTo("12345678");
    }
}