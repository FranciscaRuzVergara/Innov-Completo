package com.innovatech.task.dto;

import lombok.Data;
import com.innovatech.task.model.Task;

@Data
public class TaskResponseDTO {
    
    private Task task;

    private Object project;
}
