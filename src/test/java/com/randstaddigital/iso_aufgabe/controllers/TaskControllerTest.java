package com.randstaddigital.iso_aufgabe.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.randstaddigital.iso_aufgabe.persistence.models.Task;
import com.randstaddigital.iso_aufgabe.services.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TaskController.class)
class TaskControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private TaskService taskService;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllTasks() throws Exception {
        // Arrange
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Test Task 1");
        List<Task> taskList = List.of(task1);


        when(taskService.findAll()).thenReturn(taskList);

        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Test Task 1"));
    }

    @Test
    void testCreateTask() throws Exception {
        // Arrange
        Task taskToCreate = new Task();
        taskToCreate.setTitle("New Task");

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("New Task");

        when(taskService.save(any(Task.class))).thenReturn(savedTask);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("New Task"));
    }
}