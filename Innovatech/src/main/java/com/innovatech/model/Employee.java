package com.innovatech.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Entity
@Table(name = "Employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @Column(name = "rut", nullable = false, unique = true, length = 20)
    private String rut;

    @Column(name = "employee_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "employee_surname", nullable = false, length = 100)
    private String lastName;

    @Column(name = "employee_mail", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "total_hours")
    private Integer totalHours;

    @Column(name = "dv", length = 1)
    private String dv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    private Role role;
}
