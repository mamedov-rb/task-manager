package ru.rmamedov.app.controller.api;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.rmamedov.app.exception.BadRequestException;
import ru.rmamedov.app.exception.UserNotAuthorizedException;
import ru.rmamedov.app.model.Task;
import ru.rmamedov.app.model.user.AppUserPrincipal;
import ru.rmamedov.app.service.interfaces.ITaskService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Rustam Mamedov
 */

@Data
@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final ITaskService taskService;

    @GetMapping("/all")
    public ResponseEntity<List<Task>> findAll() {
        return new ResponseEntity<>(taskService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable final String id) {
        return new ResponseEntity<>(taskService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save/{projectId}")
    public ResponseEntity<Task> save(@Valid @RequestBody final Task task,
                                     @PathVariable final String projectId,
                                     @AuthenticationPrincipal final AppUserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new UserNotAuthorizedException("You should be logged in!");
        }
        if (task.getName() != null) {
            return new ResponseEntity<>(taskService.
                    saveWithSelfInjectionUnderUserAndProject(task, projectId, userPrincipal.getUsername()), HttpStatus.OK);
        }
        throw new BadRequestException("Saving task should't be null!");
    }

    @PutMapping("/update")
    public ResponseEntity<Task> update(@Valid @RequestBody final Task task) {
        if (task.getName() != null) {
            return new ResponseEntity<>(taskService.update(task), HttpStatus.OK);
        }
        throw new BadRequestException("Updating task should't be null!");
    }

    @PatchMapping("/patch")
    public ResponseEntity<Task> patch(@Valid @RequestBody final Task task) {
        if (task.getName() != null) {
            return new ResponseEntity<>(taskService.patch(task), HttpStatus.OK);
        }
        throw new BadRequestException("Updating task should't be null!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Task> delete(@PathVariable final String id) {
        taskService.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
