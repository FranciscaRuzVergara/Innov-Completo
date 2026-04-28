package com.innovatech.task.service;

import com.innovatech.task.repository.TaskStatusRepository;
import com.innovatech.task.model.TaskStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class TaskStatusService {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Transactional(readOnly = true)
    public List<TaskStatus> findAll() {
        return taskStatusRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TaskStatus findById(Long id) {
        return taskStatusRepository.findById(id).orElse(null);
    }
    
    public TaskStatus save(TaskStatus taskStatus) {
        return taskStatusRepository.save(taskStatus);
    }

    public void deleteTaskStatus(Long id) {
        taskStatusRepository.deleteById(id);
    }

}
