package ru.rmamedov.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rmamedov.taskmanager.model.DTO.CreationTaskRequest;
import ru.rmamedov.taskmanager.service.ProjectManagerService;

import javax.validation.Valid;

/**
 * @author Rustam Mamedov
 */

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ProjectManagerController {

    private final ProjectManagerService projectManagerService;

    @PatchMapping("/assign/username/{username}/projectId/{id}")
    public ResponseEntity assignUserToProject(@PathVariable final String username, @PathVariable String id) {
        final boolean assigned = projectManagerService.assignUserToProject(username, id);
        if (assigned) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @PostMapping(value = "/assign/task/to/user", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createTaskAndAssignToUser(@Valid @RequestBody CreationTaskRequest request,
                                                    @AuthenticationPrincipal Authentication authentication) {

        final boolean assigned = projectManagerService.createAndAssignTaskToUser(request, authentication);
        if (assigned) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @PatchMapping(value = "/reassign/task/{taskId}/user/{username}/by-project/{projectId}")
    public ResponseEntity reassignTaskToAnotherUser(@PathVariable final String taskId,
                                                    @PathVariable final String username,
                                                    @PathVariable final String projectId) {

        projectManagerService.reassignTaskToAnotherUser(taskId, username, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
