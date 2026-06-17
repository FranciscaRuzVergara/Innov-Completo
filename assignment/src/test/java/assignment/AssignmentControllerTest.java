package assignment;

import assignment.controller.AssignmentController;
import assignment.model.Assignment;
import assignment.service.AssignmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AssignmentController.class)
class AssignmentControllerTest {
@Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssignmentService assignmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Assignment sampleAssignment;
    @BeforeEach
    void setUp() {
        // Inicializamos una asignación de prueba común para reutilizar en los tests
        sampleAssignment = new Assignment();
        sampleAssignment.setId(1L);
        sampleAssignment.setEmployeeRut("11.222.333-4");
        sampleAssignment.setTaskRoleId(5L);
        sampleAssignment.setAssignedHours(10.5);
    }

   @Test
    void createAssignment_ShouldReturnCreated() throws Exception {
        when(assignmentService.createAssignment(any(Assignment.class))).thenReturn(sampleAssignment);

        mockMvc.perform(post("/assignments")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleAssignment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.employeeRut").value("11.222.333-4"))
                .andExpect(jsonPath("$.taskRoleId").value(5));
    }
    @Test
    void createAssignment_WhenServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        // Simulamos que el servicio arroja una excepción para evaluar tu bloque catch
        when(assignmentService.createAssignment(any(Assignment.class)))
                .thenThrow(new RuntimeException("Error: Employee validation failed"));

        mockMvc.perform(post("/assignments")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleAssignment)))
                .andExpect(status().isBadRequest()) // Evalúa que responda HttpStatus.BAD_REQUEST (400)
                .andExpect(content().string("Error: Employee validation failed")); // Verifica el mensaje del body
    }

    // ==========================================
    // GET /assignments
    // ==========================================
    @Test
    void getAllAssignments_ShouldReturnList_WhenNotEmpty() throws Exception {
        when(assignmentService.getAllAssignments()).thenReturn(Collections.singletonList(sampleAssignment));

        mockMvc.perform(get("/assignments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].employeeRut").value("11.222.333-4"));
    }

    @Test
    void getAllAssignments_ShouldReturnNoContent_WhenEmpty() throws Exception {
        when(assignmentService.getAllAssignments()).thenReturn(List.of()); // Lista vacía

        mockMvc.perform(get("/assignments"))
                .andExpect(status().isNoContent()); // Evalúa tu bloque if que responde HttpStatus.NO_CONTENT (204)
    }

    // ==========================================
    // DELETE /assignments/{id}
    // ==========================================
    @Test
    void deleteAssignment_ShouldReturnNoContent() throws Exception {
        // Configuramos para que el método void del service no haga nada (comportamiento normal)
        doNothing().when(assignmentService).deleteById(1L);

        mockMvc.perform(delete("/assignments/1"))
                .andExpect(status().isNoContent()); // Evalúa ResponseEntity.noContent().build() (204)
    }
}