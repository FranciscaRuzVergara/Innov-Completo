package com.innovatech.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDTO {

    private String rut;
    private String firstName;
    private String lastName;
    private String email;
    private Integer totalHours;
    private String dv;
    private Long roleId; // ID del rol
}
