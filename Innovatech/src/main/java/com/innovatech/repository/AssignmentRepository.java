package com.innovatech.repository;


import com.innovatech.model.Assignment;
import com.innovatech.model.AssignmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, AssignmentId> {
    
    Optional<Assignment> findByIdEmployeeRutAndIdTaskId(String rut, Long taskId);
}
