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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project_service.client.TaskClient;
import project_service.dto.ProjectWithTasksDTO;
import project_service.dto.TaskDTO;
import project_service.model.KPI;
import project_service.model.Project;
import project_service.model.ProjectStatus;
import project_service.repository.KPIRepository;
import project_service.repository.ProjectRepository;
import project_service.repository.ProjectStatusRepository;
import project_service.service.EmailService;
import project_service.service.ProjectService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectStatusRepository projectStatusRepository;

    @Mock
    private KPIRepository kpiRepository;

    @Mock
    private TaskClient taskClient;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ProjectService projectService;

    // ==========================================
    // TESTS PARA BÚSQUEDAS
    // ==========================================

    @Test
    void findAll_ShouldReturnProjectList() {
        when(projectRepository.findAll()).thenReturn(List.of(new Project()));
        List<Project> result = projectService.findAll();
        assertThat(result).hasSize(1);
        verify(projectRepository).findAll();
    }

    @Test
    void findById_WhenProjectExists_ShouldReturnProject() {
        Project project = new Project();
        project.setProjectId(1L);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project result = projectService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getProjectId()).isEqualTo(1L);
    }

    @Test
    void findById_WhenProjectDoesNotExist_ShouldReturnNull() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());
        Project result = projectService.findById(99L);
        assertThat(result).isNull();
    }

    // ==========================================
    // TESTS PARA GUARDAR (LÓGICA DE NEGOCIO)
    // ==========================================

    @Test
    void save_WithValidDataAndFutureEndDate_ShouldSaveWithoutEmailAlert() {
        // Preparar
        ProjectStatus statusReq = new ProjectStatus(1L, "En curso", new ArrayList<>());
        ProjectStatus statusDb = new ProjectStatus(1L, "En curso", new ArrayList<>());
        
        Project projectToSave = new Project(1L, "Web App", "Desc", LocalDate.now(), LocalDate.now().plusDays(10), statusReq, new ArrayList<>());
        
        when(projectStatusRepository.findById(1L)).thenReturn(Optional.of(statusDb));
        when(projectRepository.save(any(Project.class))).thenReturn(projectToSave);

        // Ejecutar
        Project result = projectService.save(projectToSave);

        // Verificar
        assertThat(result).isNotNull();
        verify(projectStatusRepository).findById(1L);
        verify(projectRepository).save(projectToSave);
        verify(emailService, never()).sendProjectDelayAlert(anyString(), anyString()); // No debe enviar correo
    }

    @Test
    void save_WithPastEndDate_ShouldTriggerEmailAlert() {
        // Preparar
        ProjectStatus status = new ProjectStatus(1L, "En curso", new ArrayList<>());
        LocalDate pastDate = LocalDate.now().minusDays(5); // Fecha de término en el pasado
        Project projectToSave = new Project(1L, "Web App Retrasada", "Desc", LocalDate.now().minusDays(10), pastDate, status, new ArrayList<>());
        
        when(projectStatusRepository.findById(1L)).thenReturn(Optional.of(status));
        when(projectRepository.save(any(Project.class))).thenReturn(projectToSave);

        // Ejecutar
        projectService.save(projectToSave);

        // Verificar que SÍ se envió el correo de retraso
        verify(emailService).sendProjectDelayAlert("Web App Retrasada", pastDate.toString());
    }

    @Test
    void save_WhenStatusDoesNotExist_ShouldThrowException() {
        // Preparar
        ProjectStatus invalidStatus = new ProjectStatus(99L, "Inexistente", new ArrayList<>());
        Project projectToSave = new Project(1L, "Web App", "Desc", LocalDate.now(), null, invalidStatus, new ArrayList<>());
        
        when(projectStatusRepository.findById(99L)).thenReturn(Optional.empty());

        // Ejecutar y verificar excepción
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.save(projectToSave);
        });

        assertThat(exception.getMessage()).isEqualTo("El ID de Estado de Proyecto suministrado no existe en el sistema.");
        verify(projectRepository, never()).save(any());
    }

    @Test
    void save_WithNewKPI_ShouldSaveSuccessfully() {
        // Preparar
        ProjectStatus status = new ProjectStatus(1L, "En curso", new ArrayList<>());
        KPI newKpi = new KPI(null, "Rendimiento", 100, LocalDate.now(), null); // KPI sin ID (nuevo)
        Project projectToSave = new Project(1L, "Web App", "Desc", LocalDate.now(), null, status, List.of(newKpi));
        
        when(projectStatusRepository.findById(1L)).thenReturn(Optional.of(status));
        when(projectRepository.save(any(Project.class))).thenReturn(projectToSave);

        // Ejecutar
        Project result = projectService.save(projectToSave);

        // Verificar
        assertThat(result.getKpis()).hasSize(1);
        assertThat(result.getKpis().get(0).getProject()).isEqualTo(projectToSave); // Verifica que se asignó el proyecto al KPI
    }

    // ==========================================
    // TESTS PARA ELIMINAR Y TAREAS EXTERNAS
    // ==========================================

    @Test
    void delete_ShouldCallRepositoryDelete() {
        projectService.delete(1L);
        verify(projectRepository).deleteById(1L);
    }

    @Test
    void findProjectWithTasks_ShouldReturnFullDTO() {
        // 1. Preparar el Estatus y el Proyecto
        ProjectStatus status = new ProjectStatus(1L, "En curso", new ArrayList<>());
        Project project = new Project(1L, "Web App", "Desc", LocalDate.now(), null, status, new ArrayList<>());
        
        // 2. Preparar el TaskDTO
        TaskDTO task = new TaskDTO(10L, "Tarea de Prueba", "Descripción", LocalDate.now(), null, 1L);
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
        assertThat(result.getStatusName()).isEqualTo("En curso");
        
        verify(taskClient).getTasksByProject(1L);
    }

    @Test
    void findProjectWithTasks_WhenProjectNotFound_ShouldReturnNull() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());
        
        ProjectWithTasksDTO result = projectService.findProjectWithTasks(99L);
        
        assertThat(result).isNull();
        verify(taskClient, never()).getTasksByProject(anyLong()); // No debería llamar al cliente externo si no hay proyecto
    }
}