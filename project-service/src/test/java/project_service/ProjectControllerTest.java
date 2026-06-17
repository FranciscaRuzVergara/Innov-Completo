package project_service;
import project_service.service.ProjectService;
import project_service.controller.ProjectController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getById_ShouldReturn404_WhenProjectNotFound() throws Exception {
        when(projectService.findById(99L)).thenThrow(new RuntimeException("Not found"));
        mockMvc.perform(get("/projects/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAll_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk());
    }
}