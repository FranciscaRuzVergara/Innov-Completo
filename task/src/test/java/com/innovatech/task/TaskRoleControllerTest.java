package com.innovatech.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovatech.task.controller.TaskRoleController;
import com.innovatech.task.model.Role;
import com.innovatech.task.model.Task;
import com.innovatech.task.model.TaskRole;
import com.innovatech.task.service.TaskRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskRoleController.class)
@AutoConfigureMockMvc(addFilters = false) // Evita problemas de filtros de seguridad y tokens CSRF
class TaskRoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskRoleService taskRoleService;

    private TaskRole sampleTaskRole;

    @BeforeEach
    void setUp() {
        // Creamos objetos anidados mínimos para que el modelo esté completo
        Task sampleTask = new Task();
        sampleTask.setIdTask(5L);
        sampleTask.setName("Desarrollo Backend");

        Role sampleRole = new Role(2L, "Backend Developer");

        // Inicializamos el objeto bajo prueba con el constructor de AllArgsConstructor (id, task, role)
        sampleTaskRole = new TaskRole(1L, sampleTask, sampleRole);
    }

    // ==========================================
    // GET /task-roles
    // ==========================================
    @Test
    void getAllTaskRoles_ShouldReturnList_WhenNotEmpty() throws Exception {
        when(taskRoleService.getAll()).thenReturn(Collections.singletonList(sampleTaskRole));

        mockMvc.perform(get("/task-roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].task.idTask").value(5))
                .andExpect(jsonPath("$[0].role.name").value("Backend Developer"));
    }

    @Test
    void getAllTaskRoles_ShouldReturnNoContent_WhenEmpty() throws Exception {
        when(taskRoleService.getAll()).thenReturn(List.of()); // Lista vacía

        mockMvc.perform(get("/task-roles"))
                .andExpect(status().isNoContent()); // Evalúa HttpStatus.NO_CONTENT (204)
    }

    // ==========================================
    // GET /task-roles/task/{taskId}
    // ==========================================
    @Test
    void getRolesByTask_ShouldReturnList() throws Exception {
        when(taskRoleService.getRolesByTaskId(5L)).thenReturn(Collections.singletonList(sampleTaskRole));

        mockMvc.perform(get("/task-roles/task/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].task.idTask").value(5));
    }

    // ==========================================
    // POST /task-roles/assign
    // ==========================================
    @Test
    void assignRoleToTask_ShouldReturnCreatedTaskRole() throws Exception {
        // Tu endpoint utiliza @RequestParam en lugar de @RequestBody
        when(taskRoleService.assignRoleToTask(5L, 2L)).thenReturn(sampleTaskRole);

        mockMvc.perform(post("/task-roles/assign")
                        .param("taskId", "5")    // Pasamos los parámetros por RequestParam
                        .param("roleId", "2"))
                .andExpect(status().isOk())      // Tu controller responde con ResponseEntity.ok()
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.task.idTask").value(5))
                .andExpect(jsonPath("$.role.id").value(2));
    }

    // ==========================================
    // GET /task-roles/{id}
    // ==========================================
    @Test
    void getById_WhenExists_ShouldReturnOk() throws Exception {
        when(taskRoleService.findById(1L)).thenReturn(sampleTaskRole);

        mockMvc.perform(get("/task-roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getById_WhenServiceThrowsException_ShouldReturnNotFound() throws Exception {
        // Simulamos que el servicio arroja un RuntimeException tal como indica el try-catch de tu controlador
        when(taskRoleService.findById(99L)).thenThrow(new RuntimeException("TaskRole not found"));

        mockMvc.perform(get("/task-roles/99"))
                .andExpect(status().isNotFound());
    }
}