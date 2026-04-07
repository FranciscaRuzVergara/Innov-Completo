package com.innovatech.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "assignments")
@Data
@JsonPropertyOrder({ "id", "assigned_hours"})
public class Assignment {

    @EmbeddedId
    private AssignmentId id;

    @ManyToOne
    @MapsId("employeeRut")
    @JoinColumn(name = "employee_rut")
    @JsonIgnore
    private Employee employee;

    @ManyToOne
    @MapsId("taskId")
    @JoinColumn(name = "task_id")
    @JsonIgnore
    private Task task;
    
    @Column(name = "assigned_hours")
    private Double assignedHours;
}
