package com.innovatech.task.dto;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;

import lombok.Data;


@Data
public class ProjectDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    
}
