package com.innovatech.repository;


import com.innovatech.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    Optional<Employee> findByEmail(String email);

    List<Employee> findByRoleId(Long roleId);

    boolean existsByEmail(String email);
}