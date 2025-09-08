package com.randstaddigital.iso_aufgabe.controllers;

import com.randstaddigital.iso_aufgabe.persistence.models.Task;
import com.randstaddigital.iso_aufgabe.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Dies ist der REST-Controller. Er definiert die HTTP-Endpunkte (URLs)
 * für unsere API.
 * * @RestController = Sagt Spring, dass dies ein Controller ist und die Rückgabewerte
 * automatisch in JSON umgewandelt werden sollen.
 *
 * @RequestMapping("/api/tasks") = Setzt das Basis-URL-Präfix für alle Methoden
 * in dieser Klasse.
 */
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

    /**
     * API-Endpunkt, um ALLE Tasks abzurufen.
     * Mapped auf: GET http://localhost:8080/api/tasks
     */
    @GetMapping
    public List<Task> getAllTasks() {
        // Verwendet die von JpaRepository bereitgestellte Methode findAll().
        return taskService.findAll();
    }

    /**
     * API-Endpunkt, um einen NEUEN Task zu erstellen.
     * Mapped auf: POST http://localhost:8080/api/tasks
     * * @RequestBody = Spring, nimmt den JSON-Text aus dem POST-Request und wandelt
     * ihn automatisch in ein Java-Task-Objekt um.
     */
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
