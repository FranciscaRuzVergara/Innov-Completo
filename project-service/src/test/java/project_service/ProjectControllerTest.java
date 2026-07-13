package project_service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import project_service.controller.ProjectController;
import project_service.dto.ProjectWithTasksDTO;
import project_service.model.Project;
import project_service.model.ProjectStatus;
import project_service.service.ProjectService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

    // ==========================================
    // TESTS PARA GET /projects
    // ==========================================
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAll_ShouldReturnOk() throws Exception {
        Project project = new Project();
        project.setName("App Test");
        
        when(projectService.findAll()).thenReturn(List.of(project));

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("App Test"));
    }

    // ==========================================
    // TESTS PARA GET /projects/{id}
    // ==========================================
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getById_ShouldReturnOk_WhenProjectExists() throws Exception {
        Project project = new Project();
        project.setProjectId(1L);
        project.setName("App Test");

        when(projectService.findById(1L)).thenReturn(project);

        mockMvc.perform(get("/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("App Test"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getById_ShouldReturn404_WhenProjectNotFound() throws Exception {
        when(projectService.findById(99L)).thenThrow(new RuntimeException("Not found"));
        
        mockMvc.perform(get("/projects/99"))
                .andExpect(status().isNotFound());
    }

    // ==========================================
    // TESTS PARA POST /projects
    // ==========================================
   @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void create_ShouldReturnCreated() throws Exception {
        Project projectReq = new Project();
        projectReq.setName("Nuevo Proyecto");

        Project savedProject = new Project();
        savedProject.setProjectId(1L);
        savedProject.setName("Nuevo Proyecto");

        when(projectService.save(any(Project.class))).thenReturn(savedProject);

        mockMvc.perform(post("/projects")
                .with(csrf()) // <--- AGREGAR ESTO
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.projectId").value(1L));
    }

    // ==========================================
    // TESTS PARA PUT /projects/{id}
    // ==========================================
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void update_ShouldReturnOk_WhenProjectExists() throws Exception {
        Project updateData = new Project();
        updateData.setDescription("Nueva Descripción");
        ProjectStatus newStatus = new ProjectStatus(2L, "Terminado", null);
        updateData.setProjectStatus(newStatus);

        Project existingProject = new Project();
        existingProject.setProjectId(1L);
        existingProject.setDescription("Descripción Vieja");

        Project updatedProject = new Project();
        updatedProject.setProjectId(1L);
        updatedProject.setDescription("Nueva Descripción");
        updatedProject.setProjectStatus(newStatus);

        when(projectService.findById(1L)).thenReturn(existingProject);
        when(projectService.save(any(Project.class))).thenReturn(updatedProject);

        mockMvc.perform(put("/projects/1")
                .with(csrf()) // <--- AGREGAR ESTO
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Nueva Descripción"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void update_ShouldReturn404_WhenExceptionOccurs1() throws Exception {
        Project updateData = new Project();
        
        when(projectService.findById(99L)).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(put("/projects/99")
                .with(csrf()) // <--- AGREGAR ESTO
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound());
    }

   
    // ==========================================
    // TESTS PARA DELETE /projects/{id}
    // ==========================================
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void delete_ShouldReturnNoContent_WhenSuccessful() throws Exception {
        doNothing().when(projectService).delete(1L);

        mockMvc.perform(delete("/projects/1")
                .with(csrf())) // <--- AGREGAR ESTO
                .andExpect(status().isNoContent());
        
        verify(projectService).delete(1L);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void delete_ShouldReturn404_WhenExceptionOccurs() throws Exception {
        doThrow(new RuntimeException("Error")).when(projectService).delete(99L);

        mockMvc.perform(delete("/projects/99")
                .with(csrf())) // <--- AGREGAR ESTO
                .andExpect(status().isNotFound());
    }

    // ==========================================
    // TESTS PARA GET /projects/{id}/with-tasks
    // ==========================================
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getProjectWithTasks_ShouldReturnOk_WhenFound() throws Exception {
        ProjectWithTasksDTO dto = new ProjectWithTasksDTO(1L, "Web", "Desc", LocalDate.now(), null, "En curso", new ArrayList<>(), new ArrayList<>());
        
        when(projectService.findProjectWithTasks(1L)).thenReturn(dto);

        mockMvc.perform(get("/projects/1/with-tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Web"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getProjectWithTasks_ShouldReturn404_WhenProjectReturnsNull() throws Exception {
        when(projectService.findProjectWithTasks(99L)).thenReturn(null);

        mockMvc.perform(get("/projects/99/with-tasks"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getProjectWithTasks_ShouldReturn500_WhenExceptionOccurs() throws Exception {
        when(projectService.findProjectWithTasks(anyLong())).thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(get("/projects/1/with-tasks"))
                .andExpect(status().isInternalServerError());
    }
}