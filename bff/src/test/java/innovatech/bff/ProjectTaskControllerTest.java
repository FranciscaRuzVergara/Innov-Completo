package innovatech.bff;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import innovatech.bff.controller.ProjectTasksController;
import innovatech.bff.dto.ProjectWithTasksDTO;
import innovatech.bff.service.ProjectTasksService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectTasksController.class)
class ProjectTasksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectTasksService projectTasksService;

    @Test
    void getProjectWithTasks_WhenProjectFound_Returns200OK() throws Exception {
        // Arrange
        Long projectId = 1L;
        ProjectWithTasksDTO mockDto = new ProjectWithTasksDTO(
                projectId, 
                "Proyecto Web", 
                "Desarrollo Frontend", 
                LocalDate.now(), 
                LocalDate.now().plusDays(5), 
                Collections.emptyList()
        );

        when(projectTasksService.findProjectWithTasks(projectId)).thenReturn(mockDto);

        // Act & Assert
        mockMvc.perform(get("/bff/{id}/tasks", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(projectId))
                .andExpect(jsonPath("$.name").value("Proyecto Web"))
                .andExpect(jsonPath("$.description").value("Desarrollo Frontend"));
    }

    @Test
    void getProjectWithTasks_WhenProjectNotFound_Returns404NotFound() throws Exception {
        // Arrange
        Long projectId = 2L;
        when(projectTasksService.findProjectWithTasks(projectId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/bff/{id}/tasks", projectId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProjectWithTasks_WhenExceptionThrown_Returns500InternalServerError() throws Exception {
        // Arrange
        Long projectId = 3L;
        when(projectTasksService.findProjectWithTasks(projectId))
                .thenThrow(new RuntimeException("Error simulado de conexión"));

        // Act & Assert
        mockMvc.perform(get("/bff/{id}/tasks", projectId))
                .andExpect(status().isInternalServerError());
    }
}