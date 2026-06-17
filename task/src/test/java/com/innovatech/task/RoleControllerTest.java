package com.innovatech.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovatech.task.controller.RoleController;
import com.innovatech.task.model.Role; // Ajusta el import según tu modelo real
import com.innovatech.task.service.RoleService; // Ajusta el import según tu servicio real
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoleController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva seguridad/CSRF para evitar errores 403 fortuitos
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoleService roleService;

    private ObjectMapper objectMapper;
    private Role sampleRole;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        
        // Inicializamos un objeto de prueba según tu modelo (id, name)
        sampleRole = new Role(1L, "ADMIN");
    }

    // ==========================================
    // GET /roles
    // ==========================================
    @Test
    void getAllRoles_ShouldReturnListOfRoles() throws Exception {
        // Configuramos el mock para retornar la lista con el rol de prueba
        when(roleService.findAll()).thenReturn(Collections.singletonList(sampleRole));

        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1)) // Validamos que el ID numérico sea 1
                .andExpect(jsonPath("$[0].name").value("ADMIN")); // Validamos el campo 'name'
    }

    // ==========================================
    // POST /roles
    // ==========================================
    @Test
    void createRole_ShouldReturnSavedRole() throws Exception {
        // Configuramos el mock para que cuando guarde cualquier objeto Role, devuelva el sampleRole
        when(roleService.save(any(Role.class))).thenReturn(sampleRole);

        mockMvc.perform(post("/roles")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRole)))
                .andExpect(status().isOk()) // Tu controlador devuelve HttpStatus.OK (200) al crear
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("ADMIN"));
    }
}