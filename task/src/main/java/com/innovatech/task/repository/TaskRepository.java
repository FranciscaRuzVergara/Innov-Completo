package com.innovatech.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.innovatech.task.model.Task;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository <Task, Long> {

    List<Task> findByDateCreatedBetween(LocalDate start, LocalDate end);

    
}
