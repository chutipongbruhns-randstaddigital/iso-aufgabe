package com.randstaddigital.iso_aufgabe.controllers;

import com.randstaddigital.iso_aufgabe.persistence.models.Task;
import com.randstaddigital.iso_aufgabe.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {


    private final TaskService taskService;

    /**
     * Konstruktor-Injection (bevorzugt gegenüber @Autowired auf dem Feld).
     * Spring sieht, dass der Controller ein TaskRepository braucht, und übergibt es.
     */
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping
    public List<Task> getAllTasks() {
        // Verwendet die von JpaRepository bereitgestellte Methode findAll().
        return taskService.findAll();
    }


    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        // Wir setzen die ID auf null, um sicherzustellen, dass save() eine NEUE Entität
        // erstellt (falls der User fälschlicherweise eine ID mitgesendet hat).


        // Verwendet die save()-Methode. Diese speichert den Task in der DB
        // und gibt den gespeicherten Task (inkl. der neu generierten ID) zurück.
        Task savedTask = taskService.save(task);

        // Wir geben HTTP Status 201 (Created) zusammen mit dem neuen Objekt zurück.
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }
}
