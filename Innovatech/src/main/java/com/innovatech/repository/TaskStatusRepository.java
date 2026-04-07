package com.innovatech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.innovatech.model.TaskStatus;

@Repository
public interface TaskStatusRepository extends JpaRepository <TaskStatus, Long> {
    
}
