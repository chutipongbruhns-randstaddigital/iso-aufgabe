package com.randstaddigital.iso_aufgabe.services;

import com.randstaddigital.iso_aufgabe.persistence.models.Task;
import com.randstaddigital.iso_aufgabe.persistence.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TaskServiceTest {


    @Mock
    private TaskRepository taskRepository;


    @InjectMocks
    private TaskService taskService;

    @Test
    void testFindAllTasks() {
        // Arrange
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        List<Task> mockTasks = Arrays.asList(task1, task2);

        when(taskRepository.findAll()).thenReturn(mockTasks);

        // Act
        List<Task> results = taskService.findAll();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("Task 1", results.get(0).getTitle());

        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testSaveTask() {
        // 1. Arrange (Vorbereiten)
        Task taskToSave = new Task();
        taskToSave.setTitle("Neuer Task");

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("Neuer Task");


        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // Act
        Task result = taskService.save(taskToSave);

        // Assert

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Neuer Task", result.getTitle());

        verify(taskRepository, times(1)).save(any(Task.class));
    }
}