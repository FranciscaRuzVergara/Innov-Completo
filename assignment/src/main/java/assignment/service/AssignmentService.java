package assignment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.kafka.core.KafkaTemplate;

// IMPORTS ADICIONALES PARA CAPTURAR EL TOKEN JWT DE SPRING SECURITY
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import assignment.model.Assignment;
import assignment.repository.AssignmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.urls.employee}")
    private String EMPLOYEE_SERVICE_URL;

    @Value("${app.urls.task-role}")
    private String TASK_ROLE_SERVICE_URL;

    public Assignment createAssignment(Assignment assignment) {
        // 1. EXTRAER EL TOKEN JWT DEL HILO DE EJECUCIÓN ACTUAL
        String jwtToken = "";
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthenticationToken jwtAuth) {
            jwtToken = jwtAuth.getToken().getTokenValue();
        }

        // 2. CONSTRUIR LAS CABECERAS HTTP CON EL BEARER TOKEN
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<Object> entityWithToken = new HttpEntity<>(headers);

        try {
            String employeeUrl = EMPLOYEE_SERVICE_URL + assignment.getEmployeeRut();
            restTemplate.exchange(employeeUrl, HttpMethod.GET, entityWithToken, Object.class);
            
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error en validación de empleado. Código: " + e.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException("No existe ese rut en el sistema de empleados o el servicio está caído.");
        }

        try {
            String taskRoleUrl = TASK_ROLE_SERVICE_URL + assignment.getTaskRoleId();            
            restTemplate.exchange(taskRoleUrl, HttpMethod.GET, entityWithToken, Object.class);
            
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error en validación de tareas. Código: " + e.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException("No existe ese id taskrole en el sistema de tareas.");
        }

        Assignment saved = assignmentRepository.save(assignment);
        
        try {
            String eventJson = objectMapper.writeValueAsString(saved);
            kafkaTemplate.send("topic-asignaciones", eventJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return saved;
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public void deleteById(Long id) {
        if (assignmentRepository.existsById(id)) {
            assignmentRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se encontró la asignación con ID: " + id);
        }
    }
}