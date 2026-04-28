package com.innovatech.task.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTask;

    @Column(nullable = false)
    private String name;

    private String description;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;
    
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date_finished")
    private LocalDate dateFinished;

    // Esto se queda como Long porque Proyecto será otro microservicio externo
    @Column(name = "project_id")
    private Long projectId;

    @ManyToOne
    @JoinColumn(name = "task_status_id", nullable = false)
    private TaskStatus taskStatus;

    // NUEVO: Una tarea puede tener múltiples combinaciones de roles
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @JsonIgnore // Para evitar bucles infinitos en el JSON
    private List<TaskRole> taskRoles; 
}