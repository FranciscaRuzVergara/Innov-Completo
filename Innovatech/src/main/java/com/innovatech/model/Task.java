package com.innovatech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTask;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;
    
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date_finished", nullable = true)
    private LocalDate dateFinished;

    @ManyToOne
    @JoinColumn(name = "task_status_id", nullable = false)
    private TaskStatus taskStatus;

    @Column(name = "project_id", nullable = false)
    private Long projectId;
    
}
