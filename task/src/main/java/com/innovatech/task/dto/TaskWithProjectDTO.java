package com.innovatech.task.dto;


import com.innovatech.task.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskWithProjectDTO {
    private Task task;
    private ProjectDTO project;
    
}
