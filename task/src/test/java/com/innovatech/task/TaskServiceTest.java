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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
        // Simulamos la llamada de RestTemplate
        when(restTemplate.getForObject(anyString(), eq(ProjectDTO.class))).thenReturn(mockProject);

        // Act
        TaskWithProjectDTO result = taskService.getTaskComplete(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getProject().getName()).isEqualTo("Proyecto Externo");
        assertThat(result.getTask().getIdTask()).isEqualTo(1L);
    }
}