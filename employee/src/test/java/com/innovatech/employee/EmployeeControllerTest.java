package com.innovatech.employee;

import com.innovatech.employee.controller.EmployeeController;
import com.innovatech.employee.dto.EmployeeResponseDTO;
import com.innovatech.employee.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean; 
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // <--- Cambiado de @MockBean a @MockitoBean
    private EmployeeService employeeService;

    @Test
    void getEmployeeByRut_ShouldReturnOk() throws Exception {
        EmployeeResponseDTO response = new EmployeeResponseDTO("12345678", "Juan", "Pérez", "juan@test.com", 40, "9");
        
        when(employeeService.getEmployeeByRut("12345678")).thenReturn(Optional.of(response));

        mockMvc.perform(get("/employees/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Juan"));
    }
}