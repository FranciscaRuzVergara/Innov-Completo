package com.innovatech.task;


import com.innovatech.task.service.TaskService;
import org.junit.jupiter.api.Test;
import com.innovatech.task.controller.TaskController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getById_ShouldReturn404_WhenTaskDoesNotExist() throws Exception {
        // Configuramos el mock para que devuelva null (tu controller maneja eso con un 404)
        when(taskService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/tasks/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("A task with this ID does not exist."));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAll_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/tasks/all"))
                .andExpect(status().isOk());
    }
}