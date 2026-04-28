package com.innovatech.employee.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// Asegúrate de que este import apunte a tu repositorio real
import com.innovatech.employee.repository.EmployeeRepository; 

@Component
public class AssignmentListener {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "topic-asignaciones", groupId = "empleados-group")
    public void handleNewAssignment(String message) {
        try {
            // 1. Leer el evento que viajó por la red
            JsonNode event = objectMapper.readTree(message);
            String rut = event.get("employeeRut").asText();
            Double hours = event.get("assignedHours").asDouble();

            // 2. Actualizar las horas del empleado en db_employees
            employeeRepository.findById(rut).ifPresent(employee -> {
                int newTotal = employee.getTotalHours() + hours.intValue();
                employee.setTotalHours(newTotal);
                employeeRepository.save(employee);
                System.out.println("✅ Horas actualizadas para el RUT: " + rut);
            });

        } catch (Exception e) {
            System.err.println("❌ Error procesando asignación: " + e.getMessage());
        }
    }
}