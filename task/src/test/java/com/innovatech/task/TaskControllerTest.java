package com.innovatech.task;


import com.innovatech.task.service.TaskService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.innovatech.task.controller.TaskController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import com.innovatech.task.model.Task;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
@WebMvcTest(controllers = TaskController.class)
@AutoConfigureMockMvc(addFilters = false) // <--- ESTO DESACTIVA TODO EL FLUJO DE SEGURIDAD/CSRF PARA EL TEST
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    private ObjectMapper objectMapper;
    private Task sampleTask;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        sampleTask = new Task();
        sampleTask.setIdTask(1L);
        sampleTask.setName("Test Task");
        sampleTask.setProjectId(100L); // Nos aseguramos de inicializarlo aquí
        sampleTask.setDateCreated(LocalDate.of(2026, 6, 17));
    }

    // ==========================================
    // GET /tasks/all
    // ==========================================
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAll_ShouldReturnOk() throws Exception {
        when(taskService.findAll()).thenReturn(Collections.singletonList(sampleTask));

        mockMvc.perform(get("/tasks/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Task"));
    }

    // ==========================================
    // GET /tasks/{id}
    // ==========================================
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getById_ShouldReturn404_WhenTaskDoesNotExist() throws Exception {
        when(taskService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/tasks/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("A task with this ID does not exist."));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getById_ShouldReturnOk_WhenTaskExists() throws Exception {
        when(taskService.findById(1L)).thenReturn(sampleTask);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Task"));
    }

    // ==========================================
    // POST /tasks/save
    // ==========================================
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void save_ShouldReturnSavedTask() throws Exception {
        when(taskService.save(any(Task.class))).thenReturn(sampleTask);

        // Ya no necesitas el .with(csrf()) porque "addFilters = false" se encarga de todo
        mockMvc.perform(post("/tasks/save")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Task"));
    }

   // ==========================================
    // PUT /tasks/update/{id}
    // ==========================================
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void update_ShouldReturnOk_WhenTaskExists() throws Exception {
        when(taskService.findById(1L)).thenReturn(sampleTask);
        when(taskService.save(any(Task.class))).thenReturn(sampleTask);

        mockMvc.perform(put("/tasks/update/1")
                        .with(csrf()) // <--- SOLUCIÓN AL 403: Agrega esto
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleTask)))
                .andExpect(status().isOk());
    }

    // ==========================================
    // DELETE /tasks/{id}
    // ==========================================
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void delete_ShouldReturnOk_WhenTaskExists() throws Exception {
        when(taskService.findById(1L)).thenReturn(sampleTask);
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/tasks/1")
                        .with(csrf())) // <--- SOLUCIÓN AL 403: Agrega esto
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully."));
    }

    // ==========================================
    // GET /tasks/project/{projectId}
    // ==========================================
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getTasksByProject_ShouldReturnList() throws Exception {
        // Aseguramos explícitamente el valor dentro del bloque del test por si acaso
        sampleTask.setProjectId(100L); 
        
        when(taskService.getTasksByProjectId(100L)).thenReturn(Collections.singletonList(sampleTask));

        mockMvc.perform(get("/tasks/project/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].projectId").value(100)); // Cambiado a 100 sin la 'L' para que Jackson lo lea bien como número entero en el JSON
    }
}