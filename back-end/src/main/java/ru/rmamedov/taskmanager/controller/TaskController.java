package ru.rmamedov.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rmamedov.taskmanager.model.DTO.TaskProjection;
import ru.rmamedov.taskmanager.service.TaskService;

import java.util.Set;

/**
 * @author Rustam Mamedov
 */


@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteById(@PathVariable final String id) {
        taskService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/all/by/assignedTo/projectId/{projectId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE) // TODO: unused.
    public ResponseEntity<Set<TaskProjection>> findAllByUserId(@Nullable @AuthenticationPrincipal Authentication authentication,
                                                               @PathVariable final String projectId) {

        return new ResponseEntity<>(taskService.findAllByAssignedToAndProjectAsDTO(authentication, projectId), HttpStatus.OK);
    }

}
