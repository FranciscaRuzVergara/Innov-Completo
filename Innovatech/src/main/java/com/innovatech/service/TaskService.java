package com.innovatech.service;

import org.springframework.stereotype.Service;
import com.innovatech.model.Task;
import com.innovatech.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> getTasksByDateRange(LocalDate start, LocalDate end) {
        return taskRepository.findByDateCreatedBetween(start, end);
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getTasksByProjectId(Long projectId){
        return taskRepository.findByProjectId(projectId);
    }
}
