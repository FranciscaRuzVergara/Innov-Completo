package com.innovatech.employee;

import com.innovatech.employee.controller.EmployeeController;
import com.innovatech.employee.dto.EmployeeResponseDTO;
import com.innovatech.employee.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean; 
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovatech.employee.dto.EmployeeRequestDTO;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.http.MediaType;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

   @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    private ObjectMapper objectMapper;
    private EmployeeResponseDTO sampleResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        // 7 parámetros: id, rut, firstName, lastName, email, totalHours, dv
        sampleResponse = new EmployeeResponseDTO(1L, "12345678", "Juan", "Pérez", "juan@test.com", 40, "9");
    }

 
    @Test
    void getAllEmployees_ShouldReturnList() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(List.of(sampleResponse));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].rut").value("12345678"))
                .andExpect(jsonPath("$[0].firstName").value("Juan"));
    }

    
    @Test
    void getEmployeeById_WhenExists_ShouldReturnOk() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(sampleResponse));

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("juan@test.com"));
    }

    @Test
    void getEmployeeById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        when(employeeService.getEmployeeById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/employees/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getEmployeeByRut_ShouldReturnOk() throws Exception {
        when(employeeService.getEmployeeByRut("12345678")).thenReturn(Optional.of(sampleResponse));

        mockMvc.perform(get("/employees/rut/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Juan"));
    }

    @Test
    void getEmployeeByRut_WhenNotExists_ShouldReturnNotFound() throws Exception {
        when(employeeService.getEmployeeByRut("00000000")).thenReturn(Optional.empty());

        mockMvc.perform(get("/employees/rut/00000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getEmployeeByEmail_WhenExists_ShouldReturnOk() throws Exception {
        when(employeeService.getEmployeeByEmail("juan@test.com")).thenReturn(Optional.of(sampleResponse));

        mockMvc.perform(get("/employees/email/juan@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@test.com"));
    }

    @Test
    void createEmployee_ShouldReturnCreated() throws Exception {
        // Corregido: Se pasa null en el ID ya que usualmente el cliente no lo envía al crear, pero el constructor pide 7 campos.
        EmployeeRequestDTO request = new EmployeeRequestDTO(null, "12345678", "Juan", "Pérez", "juan@test.com", 40, "9");
        when(employeeService.createEmployee(any(EmployeeRequestDTO.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void createEmployee_WhenConflict_ShouldReturnConflict() throws Exception {
        EmployeeRequestDTO request = new EmployeeRequestDTO(null, "12345678", "Juan", "Pérez", "juan@test.com", 40, "9");
        when(employeeService.createEmployee(any(EmployeeRequestDTO.class))).thenThrow(new RuntimeException("Conflict"));

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateEmployee_ShouldReturnUpdated() throws Exception {
        EmployeeRequestDTO request = new EmployeeRequestDTO(1L, "12345678", "Juan", "Pérez", "juan@cambiado.com", 45, "9");
        EmployeeResponseDTO updatedResponse = new EmployeeResponseDTO(1L, "12345678", "Juan", "Pérez", "juan@cambiado.com", 45, "9");
        
        when(employeeService.updateEmployee(eq(1L), any(EmployeeRequestDTO.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@cambiado.com"))
                .andExpect(jsonPath("$.totalHours").value(45));
    }


    @Test
    void deleteEmployee_ShouldReturnNoContent() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isNoContent());
    }
 

    
}