package com.randstaddigital.iso_aufgabe.services;

import com.randstaddigital.iso_aufgabe.persistence.models.Task;
import com.randstaddigital.iso_aufgabe.persistence.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task save(Task task) {
        task.setId(null);
        return taskRepository.save(task);
    }
}
