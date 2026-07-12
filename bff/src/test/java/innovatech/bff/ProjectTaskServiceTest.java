package innovatech.bff;
import innovatech.bff.client.ProjectClient;
import innovatech.bff.client.TaskClient;
import innovatech.bff.dto.ProjectDTO;
import innovatech.bff.dto.ProjectWithTasksDTO;
import innovatech.bff.dto.TaskDTO;
import innovatech.bff.service.ProjectTasksService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectTaskServiceTest {

    @Mock
    private ProjectClient projectClient;

    @Mock
    private TaskClient taskClient;

    @InjectMocks
    private ProjectTasksService projectTasksService;

    @Test
    void findProjectWithTasks_ProjectExists_ReturnsProjectWithTasksDTO() {
        // Arrange (Preparar)
        Long projectId = 1L;
        ProjectDTO mockProject = new ProjectDTO(projectId, "Proyecto A", "Descripción A", LocalDate.now(), LocalDate.now().plusDays(10));
        
        TaskDTO mockTask = new TaskDTO(100L, "Tarea 1", "Desc Tarea", LocalDate.now(), LocalDate.now().plusDays(2), projectId);
        List<TaskDTO> mockTasks = Arrays.asList(mockTask);

        when(projectClient.getProjectById(projectId)).thenReturn(mockProject);
        when(taskClient.getTasksByProject(projectId)).thenReturn(mockTasks);

        // Act (Actuar)
        ProjectWithTasksDTO result = projectTasksService.findProjectWithTasks(projectId);

        // Assert (Afirmar)
        assertNotNull(result);
        assertEquals(projectId, result.getProjectId());
        assertEquals("Proyecto A", result.getName());
        assertEquals(1, result.getTasks().size());
        assertEquals("Tarea 1", result.getTasks().get(0).getName());
        
        // Verificar que los clientes fueron llamados correctamente
        verify(projectClient, times(1)).getProjectById(projectId);
        verify(taskClient, times(1)).getTasksByProject(projectId);
    }

    @Test
    void findProjectWithTasks_ProjectDoesNotExist_ReturnsNull() {
        // Arrange
        Long projectId = 99L;
        when(projectClient.getProjectById(projectId)).thenReturn(null);

        // Act
        ProjectWithTasksDTO result = projectTasksService.findProjectWithTasks(projectId);

        // Assert
        assertNull(result);
        
        // Verificar que getProjectById fue llamado, pero getTasksByProject NUNCA se llamó
        verify(projectClient, times(1)).getProjectById(projectId);
        verify(taskClient, never()).getTasksByProject(anyLong());
    }
}