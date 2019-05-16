package ru.rmamedov.app.controller.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.rmamedov.app.exception.TaskAlreadyExistsException;
import ru.rmamedov.app.exception.TaskNotFoundException;
import ru.rmamedov.app.model.Task;
import ru.rmamedov.app.service.interfaces.ITaskService;

import java.util.List;

/**
 * @author Rustam Mamedov
 */

@Slf4j
@RestController
@RequestMapping("/api/task")
public class TaskController {

    private ITaskService taskService;

    @Autowired
    public TaskController(ITaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> findAll() {
        try {
            return new ResponseEntity<>(taskService.findAll(), HttpStatus.OK);
        } catch (TaskNotFoundException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable final String id) {
        if (id == null || id.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(taskService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save/{projectId}")
    public ResponseEntity<Task> save(@RequestBody final Task task,
                                     @PathVariable final String projectId, final Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        if (task != null && projectId != null && !projectId.isEmpty()) {
            try {
                return new ResponseEntity<>(taskService.saveWithSelfInjectionUnderUserAndProject(task, projectId, authentication.getName()),HttpStatus.OK);
            } catch (TaskAlreadyExistsException ex) {
                ex.printStackTrace();
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/update")
    public ResponseEntity<Task> update(@RequestBody final Task task) {
        if (task == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(taskService.update(task), HttpStatus.OK);
    }

    @PatchMapping("/patch")
    public ResponseEntity<Task> patch(@RequestBody final Task task) {
        if (task == null) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(taskService.patch(task), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Task> delete(@PathVariable final String id) {
        if (id == null || id.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        taskService.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
