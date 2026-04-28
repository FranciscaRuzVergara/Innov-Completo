package com.innovatech.task.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "task_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Este ID es el que usará el MS Assignment

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role; 
}