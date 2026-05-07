package com.innovatech.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.innovatech.task.model.TaskStatus;

@Repository
public interface TaskStatusRepository extends JpaRepository <TaskStatus, Long> {
    
}
