package com.innovatech.task;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovatech.task.controller.TaskStatusController;
import com.innovatech.task.model.TaskStatus; // Ajusta el import según tu modelo real
import com.innovatech.task.service.TaskStatusService; // Ajusta el import según tu servicio real
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskStatusController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva de raíz la seguridad perimetral para evitar errores 403 fortuitos
class TaskStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskStatusService taskStatusService;

    private ObjectMapper objectMapper;
    private TaskStatus sampleStatus;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        
        // Inicializamos el objeto bajo prueba usando (idTaskStatus, status, tasks)
        sampleStatus = new TaskStatus(1L, "En Progreso", new ArrayList<>());
    }

    // ==========================================
    // GET /task-status/all
    // ==========================================
    @Test
    void getAll_ShouldReturnListOfStatuses() throws Exception {
        when(taskStatusService.findAll()).thenReturn(Collections.singletonList(sampleStatus));

        mockMvc.perform(get("/task-status/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTaskStatus").value(1))
                .andExpect(jsonPath("$[0].status").value("En Progreso"));
    }

    // ==========================================
    // GET /task-status/{id}
    // ==========================================
    @Test
    void getById_WhenExists_ShouldReturnStatus() throws Exception {
        when(taskStatusService.findById(1L)).thenReturn(sampleStatus);

        mockMvc.perform(get("/task-status/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTaskStatus").value(1))
                .andExpect(jsonPath("$.status").value("En Progreso"));
    }

    @Test
    void getById_WhenDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(taskStatusService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/task-status/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("A task status with this ID does not exist."));
    }

    // ==========================================
    // POST /task-status/save
    // ==========================================
    @Test
    void save_ShouldReturnSavedStatus() throws Exception {
        when(taskStatusService.save(any(TaskStatus.class))).thenReturn(sampleStatus);

        mockMvc.perform(post("/task-status/save")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStatus)))
                .andExpect(status().isOk()) // Tu controlador devuelve directamente el objeto (HttpStatus 200 OK)
                .andExpect(jsonPath("$.idTaskStatus").value(1))
                .andExpect(jsonPath("$.status").value("En Progreso"));
    }

    // ==========================================
    // PUT /task-status/update/{id}
    // ==========================================
    @Test
    void update_WhenExists_ShouldReturnUpdatedStatus() throws Exception {
        when(taskStatusService.findById(1L)).thenReturn(sampleStatus);
        when(taskStatusService.save(any(TaskStatus.class))).thenReturn(sampleStatus);

        mockMvc.perform(put("/task-status/update/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStatus)))
                .andExpect(status().isOk());
    }

    @Test
    void update_WhenDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(taskStatusService.findById(99L)).thenReturn(null);

        mockMvc.perform(put("/task-status/update/99")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStatus)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot update: A task status with this ID does not exist."));
    }

    // ==========================================
    // DELETE /task-status/{id}
    // ==========================================
    @Test
    void delete_WhenExists_ShouldReturnSuccessMessage() throws Exception {
        when(taskStatusService.findById(1L)).thenReturn(sampleStatus);
        doNothing().when(taskStatusService).deleteTaskStatus(1L);

        mockMvc.perform(delete("/task-status/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task status deleted successfully."));
    }

    @Test
    void delete_WhenDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(taskStatusService.findById(99L)).thenReturn(null);

        mockMvc.perform(delete("/task-status/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cannot delete: A task status with this ID does not exist."));
    }
}