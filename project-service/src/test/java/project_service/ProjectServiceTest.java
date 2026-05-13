package project_service;

import project_service.client.TaskClient;
import project_service.dto.ProjectWithTasksDTO;
import project_service.dto.TaskDTO;
import project_service.model.Project;
import project_service.model.ProjectStatus;
import project_service.repository.ProjectRepository;
import project_service.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskClient taskClient;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void findProjectWithTasks_ShouldReturnFullDTO() {
        // 1. Preparar el Estatus y el Proyecto
        ProjectStatus status = new ProjectStatus(1L, "En curso", new ArrayList<>());
        Project project = new Project(1L, "Web App", "Desc", LocalDate.now(), null, status, new ArrayList<>());
        
        // 2. Preparar el TaskDTO con TODOS los parámetros del constructor 
        // Orden: idTask, name, description, dateCreated, dateFinished, projectId
        TaskDTO task = new TaskDTO(
            10L, 
            "Tarea de Prueba", 
            "Descripción de la tarea", 
            LocalDate.now(), 
            null, 
            1L
        );
        
        List<TaskDTO> tasks = List.of(task);

        // 3. Configurar los Mocks
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskClient.getTasksByProject(1L)).thenReturn(tasks);

        // 4. Ejecutar
        ProjectWithTasksDTO result = projectService.findProjectWithTasks(1L);

        // 5. Verificar
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Web App");
        assertThat(result.getTasks()).hasSize(1);
        assertThat(result.getTasks().get(0).getName()).isEqualTo("Tarea de Prueba");
        
        verify(taskClient).getTasksByProject(1L);
    }
}