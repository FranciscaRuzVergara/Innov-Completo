package com.innovatech.task;

import com.innovatech.task.dto.ProjectDTO;
import com.innovatech.task.dto.TaskWithProjectDTO;
import com.innovatech.task.model.Task;
import com.innovatech.task.model.TaskStatus;
import com.innovatech.task.service.TaskService;
import com.innovatech.task.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getTaskComplete_ShouldReturnTaskWithProjectData() {
        // Arrange
        TaskStatus status = new TaskStatus(1L, "Pending", null);
        Task task = new Task();
        task.setIdTask(1L);
        task.setProjectId(100L);
        task.setTaskStatus(status);

        ProjectDTO mockProject = new ProjectDTO();
        mockProject.setProjectId(100L);
        mockProject.setName("Proyecto Externo");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(restTemplate.getForObject(anyString(), eq(ProjectDTO.class))).thenReturn(mockProject);

        // Act
        TaskWithProjectDTO result = taskService.getTaskComplete(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getProject().getName()).isEqualTo("Proyecto Externo");
        assertThat(result.getTask().getIdTask()).isEqualTo(1L);
    }

    // ==========================================
    // NUEVOS TESTS PARA SUBIR COBERTURA
    // ==========================================

    @Test
    void getTaskComplete_WhenTaskDoesNotExist_ShouldReturnNull() {
        // Arrange: Forzamos a que el repositorio devuelva un Optional vacío
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        TaskWithProjectDTO result = taskService.getTaskComplete(1L);

        // Assert: Validamos que devuelva null y que no intente llamar al RestTemplate
        assertThat(result).isNull();
        verify(restTemplate, never()).getForObject(anyString(), any());
    }

    @Test
    void getTaskComplete_WhenTaskHasNoProjectId_ShouldReturnNull() {
        // Arrange: Tarea sin ID de proyecto
        Task task = new Task();
        task.setIdTask(1L);
        task.setProjectId(null); // <-- Condición clave para romper el segundo IF

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        TaskWithProjectDTO result = taskService.getTaskComplete(1L);

        // Assert
        assertThat(result).isNull();
        verify(restTemplate, never()).getForObject(anyString(), any());
    }

    @Test
    void getTasksByDateRange_ShouldReturnListOfTasks() {
        // Arrange
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(5);
        List<Task> mockTasks = List.of(new Task(), new Task());
        
        when(taskRepository.findByDateCreatedBetween(start, end)).thenReturn(mockTasks);

        // Act
        List<Task> result = taskService.getTasksByDateRange(start, end);

        // Assert
        assertThat(result).hasSize(2);
        verify(taskRepository, times(1)).findByDateCreatedBetween(start, end);
    }

    @Test
    void deleteTask_ShouldCallRepositoryDelete() {
        // Arrange
        Long taskId = 1L;
        doNothing().when(taskRepository).deleteById(taskId);

        // Act
        taskService.deleteTask(taskId);

        // Assert: Garantiza que la línea de borrado fue ejecutada
        verify(taskRepository, times(1)).deleteById(taskId);
    }
}