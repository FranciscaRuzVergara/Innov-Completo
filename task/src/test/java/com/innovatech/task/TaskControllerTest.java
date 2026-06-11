package com.innovatech.task;


import com.innovatech.task.service.TaskService;
import org.junit.jupiter.api.Test;
import com.innovatech.task.controller.TaskController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
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
    void getById_ShouldReturn404_WhenTaskDoesNotExist() throws Exception {
        // Configuramos el mock para que devuelva null (tu controller maneja eso con un 404)
        when(taskService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("A task with this ID does not exist."));
    }

    @Test
    void getAll_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/tasks/all"))
                .andExpect(status().isOk());
    }
}