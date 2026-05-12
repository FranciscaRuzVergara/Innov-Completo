package com.innovatech.task.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovatech.task.repository.TaskRoleRepository;
import com.innovatech.task.repository.TaskRepository;
import com.innovatech.task.model.TaskStatus;

@Component
public class AssignmentListener {

    @Autowired
    private TaskRoleRepository taskRoleRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "topic-asignaciones", groupId = "tareas-group")
    public void handleNewAssignment(String message) {
        try {
            JsonNode event = objectMapper.readTree(message);
            Long taskRoleId = event.get("taskRoleId").asLong();

            taskRoleRepository.findById(taskRoleId).ifPresent(taskRole -> {
                
                // 1. Instanciamos el estado y usamos el setter del ID numérico
                TaskStatus inProgressStatus = new TaskStatus();
                inProgressStatus.setIdTaskStatus(2L); 

                // 2. Le pasamos el OBJETO al setter correcto de la Tarea
                taskRole.getTask().setTaskStatus(inProgressStatus); 
                
                // 3. Guardamos
                taskRepository.save(taskRole.getTask());
                
                System.out.println("✅ Tarea actualizada a En Progreso para el TaskRole ID: " + taskRoleId);
            });

        } catch (Exception e) {
            System.err.println("❌ Error procesando asignación en Tareas: " + e.getMessage());
        }
    }
}