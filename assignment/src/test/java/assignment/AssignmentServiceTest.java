package assignment;

import assignment.model.Assignment;
import assignment.repository.AssignmentRepository;
import assignment.service.AssignmentService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    @Mock
    private AssignmentRepository repository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AssignmentService assignmentService;

    @BeforeEach
    void setUp() {
        // Inyectamos valores falsos para los @Value ya que @InjectMocks no los lee por defecto
        ReflectionTestUtils.setField(assignmentService, "EMPLOYEE_SERVICE_URL", "http://employee/");
        ReflectionTestUtils.setField(assignmentService, "TASK_ROLE_SERVICE_URL", "http://task-role/");
    }

    // ==========================================
    // TESTS PARA CREATE
    // ==========================================
    @Test
    void createAssignment_ShouldSaveAndSendKafkaEvent() throws Exception {
        // Arrange
        Assignment assignment = new Assignment();
        assignment.setEmployeeRut("12345678-9");
        assignment.setTaskRoleId(1L);
        
        // Simulamos que el RestTemplate responde OK para ambas llamadas (Empleado y Tarea)
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(new ResponseEntity<>(new Object(), HttpStatus.OK));

        when(repository.save(any(Assignment.class))).thenReturn(assignment);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"json\":\"mock\"}");

        // Act
        Assignment result = assignmentService.createAssignment(assignment);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEmployeeRut()).isEqualTo("12345678-9");
        
        verify(kafkaTemplate, times(1)).send(eq("topic-asignaciones"), anyString());
        verify(repository, times(1)).save(assignment);
    }

    @Test
    void createAssignment_WhenEmployeeValidationFails_ShouldThrowException() {
        Assignment assignment = new Assignment();
        assignment.setEmployeeRut("12345678-9");

        // Simulamos que el llamado al servicio de empleados falla con un error 404
        when(restTemplate.exchange(contains("employee"), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assignmentService.createAssignment(assignment);
        });

        assertTrue(exception.getMessage().contains("Error en validación de empleado"));
    }

    @Test
    void createAssignment_WhenTaskRoleValidationFails_ShouldThrowException() {
        Assignment assignment = new Assignment();
        assignment.setEmployeeRut("12345678-9");
        assignment.setTaskRoleId(1L);

        // La primera llamada (empleados) es exitosa
        when(restTemplate.exchange(contains("employee"), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(new ResponseEntity<>(new Object(), HttpStatus.OK));
        
        // La segunda llamada (task role) falla
        when(restTemplate.exchange(contains("task-role"), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class)))
                .thenThrow(new RuntimeException("Conexión rechazada"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assignmentService.createAssignment(assignment);
        });

        assertTrue(exception.getMessage().contains("No existe ese id taskrole en el sistema de tareas"));
    }

    // ==========================================
    // TESTS PARA GET ALL
    // ==========================================
    @Test
    void getAllAssignments_ShouldReturnList() {
        Assignment assignment = new Assignment();
        assignment.setId(1L);
        when(repository.findAll()).thenReturn(Collections.singletonList(assignment));

        List<Assignment> result = assignmentService.getAllAssignments();

        assertThat(result).hasSize(1);
        verify(repository, times(1)).findAll();
    }

    // ==========================================
    // TESTS PARA DELETE
    // ==========================================
    @Test
    void deleteById_ShouldDelete_WhenExists() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        assignmentService.deleteById(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_ShouldThrowException_WhenDoesNotExist() {
        when(repository.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assignmentService.deleteById(99L);
        });

        assertTrue(exception.getMessage().contains("No se encontró la asignación con ID: 99"));
        verify(repository, never()).deleteById(anyLong()); // Validamos que NO se llamó al delete
    }
}