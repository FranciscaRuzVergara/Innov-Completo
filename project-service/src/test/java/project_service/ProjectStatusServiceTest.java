package project_service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project_service.model.ProjectStatus;
import project_service.repository.ProjectStatusRepository;
import project_service.service.ProjectStatusService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectStatusServiceTest {

    @Mock
    private ProjectStatusRepository projectStatusRepository;

    @InjectMocks
    private ProjectStatusService projectStatusService;

    // ==========================================
    // TEST PARA findAll()
    // ==========================================
    @Test
    void findAll_ShouldReturnListOfStatuses() {
        // Preparar
        ProjectStatus status1 = new ProjectStatus(1L, "En curso", new ArrayList<>()); //[cite: 7]
        ProjectStatus status2 = new ProjectStatus(2L, "Terminado", new ArrayList<>()); //[cite: 7]
        when(projectStatusRepository.findAll()).thenReturn(List.of(status1, status2));

        // Ejecutar
        List<ProjectStatus> result = projectStatusService.findAll(); //[cite: 6]

        // Verificar
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getStatusName()).isEqualTo("En curso"); //[cite: 7]
        verify(projectStatusRepository).findAll();
    }

    // ==========================================
    // TESTS PARA findById()
    // ==========================================
    @Test
    void findById_WhenStatusExists_ShouldReturnStatus() {
        // Preparar
        ProjectStatus status = new ProjectStatus(1L, "En curso", new ArrayList<>()); //[cite: 7]
        when(projectStatusRepository.findById(1L)).thenReturn(Optional.of(status));

        // Ejecutar
        ProjectStatus result = projectStatusService.findById(1L); //[cite: 6]

        // Verificar
        assertThat(result).isNotNull();
        assertThat(result.getProjectStatusId()).isEqualTo(1L); //[cite: 7]
        assertThat(result.getStatusName()).isEqualTo("En curso"); //[cite: 7]
        verify(projectStatusRepository).findById(1L);
    }

    @Test
    void findById_WhenStatusDoesNotExist_ShouldReturnNull() {
        // Preparar
        when(projectStatusRepository.findById(99L)).thenReturn(Optional.empty());

        // Ejecutar
        ProjectStatus result = projectStatusService.findById(99L); //[cite: 6]

        // Verificar
        assertThat(result).isNull();
        verify(projectStatusRepository).findById(99L);
    }

    // ==========================================
    // TEST PARA save()
    // ==========================================
    @Test
    void save_ShouldSaveAndReturnStatus() {
        // Preparar
        ProjectStatus statusToSave = new ProjectStatus(null, "Planificado", new ArrayList<>()); //[cite: 7]
        ProjectStatus savedStatus = new ProjectStatus(1L, "Planificado", new ArrayList<>()); //[cite: 7]
        when(projectStatusRepository.save(any(ProjectStatus.class))).thenReturn(savedStatus);

        // Ejecutar
        ProjectStatus result = projectStatusService.save(statusToSave); //[cite: 6]

        // Verificar
        assertThat(result).isNotNull();
        assertThat(result.getProjectStatusId()).isEqualTo(1L); //[cite: 7]
        assertThat(result.getStatusName()).isEqualTo("Planificado"); //[cite: 7]
        verify(projectStatusRepository).save(statusToSave);
    }

    // ==========================================
    // TEST PARA delete()
    // ==========================================
    @Test
    void delete_ShouldCallRepositoryDelete() {
        // Ejecutar
        projectStatusService.delete(1L); //[cite: 6]

        // Verificar
        verify(projectStatusRepository).deleteById(1L);
    }
}