package assignment;

import assignment.model.Assignment;
import assignment.repository.AssignmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import assignment.service.AssignmentService;

import static org.assertj.core.api.Assertions.assertThat;
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

    @InjectMocks
    private AssignmentService assignmentService;

    @Test
    void createAssignment_ShouldSaveAndSendKafkaEvent() throws Exception {
        // Arrange
        Assignment assignment = new Assignment();
        assignment.setEmployeeRut("12345678-9");
        assignment.setTaskRoleId(1L);
        
        when(repository.save(any(Assignment.class))).thenReturn(assignment);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"json\":\"mock\"}");

        // Act
        Assignment result = assignmentService.createAssignment(assignment);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEmployeeRut()).isEqualTo("12345678-9");
        
        // Verificamos que se intentó enviar a Kafka al tópico correcto
        verify(kafkaTemplate, times(1)).send(eq("topic-asignaciones"), anyString());
        verify(repository, times(1)).save(assignment);
    }
}