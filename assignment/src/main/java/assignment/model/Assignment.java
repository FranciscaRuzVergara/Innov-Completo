package assignment.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "assignments")
@Data
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_rut", nullable = false)
    private String employeeRut;

    @Column(name = "task_role_id", nullable = false)
    private Long taskRoleId;

    @Column(name = "assigned_hours")
    private Double assignedHours;
}