package ru.rmamedov.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rmamedov.taskmanager.model.DTO.SaveCommentRequest;
import ru.rmamedov.taskmanager.model.DTO.SaveTaskRequest;
import ru.rmamedov.taskmanager.service.ProjectManagerService;
import ru.rmamedov.taskmanager.service.ProjectService;
import ru.rmamedov.taskmanager.service.UserService;

import javax.validation.Valid;

/**
 * @author Rustam Mamedov
 */

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ProjectManagerController {

    private final ProjectManagerService projectManagerService;

    private final ProjectService projectService;

    private final UserService userService;

    @PatchMapping("/assign/username/{username}/projectId/{id}")
    public ResponseEntity assignUserToProject(@PathVariable final String username, @PathVariable String id) {
        final boolean assigned = projectManagerService.assignUserToProject(username, id);
        if (assigned) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @PatchMapping("/leave/username/{username}/projectId/{id}")
    public ResponseEntity leaveProjectWithTasksUnderUser(@PathVariable final String username, @PathVariable String id) {
        final boolean assigned = projectManagerService.leaveProjectUnderUser(username, id);
        if (assigned) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @PatchMapping("/leave/all/projects/user/{username}")
    public ResponseEntity leaveAllProjectsWithTasks(@PathVariable final String username) {
        projectManagerService.leaveAllProjectsUnderUser(username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/assign/task/to/user", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createTaskAndAssignToUser(@Valid @RequestBody SaveTaskRequest request,
                                                    @AuthenticationPrincipal Authentication authentication) {

        final boolean assigned = projectManagerService.createAndAssignTaskToUser(request, authentication);
        if (assigned) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @PatchMapping("/reassign/task/{taskId}/user/{username}/by-project/{projectId}")
    public ResponseEntity reassignTaskToAnotherUser(@PathVariable final String taskId,
                                                    @PathVariable final String username,
                                                    @PathVariable final String projectId) {

        projectManagerService.reassignTaskToAnotherUser(taskId, username, projectId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/project/id/{id}/user/{username}")
    public ResponseEntity deleteProjectWithTasksUnderUser(@PathVariable String id,
                                                          @PathVariable final String username) {

        projectManagerService.leaveProjectUnderUser(username, id); //TODO: do in Transactional
        projectService.deleteProjectById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping(value = "/comment/save", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity saveCommentUnderUserProjectTask(@Valid @RequestBody SaveCommentRequest request,
                                                          @AuthenticationPrincipal Authentication authentication) {

        final boolean saved = projectManagerService.saveCommentUnderUserAndTask(request, authentication);
        if (saved) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @DeleteMapping("/comment/delete/{id}")
    public ResponseEntity deleteCommentUnderTasksAndUser(@PathVariable String id) {
        final boolean removed = projectManagerService.removeCommentUnderUserAndTask(id);
        if (removed) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @DeleteMapping("/user/delete/{username}")
    public ResponseEntity deleteUserWithProjectsTasksComments(@PathVariable String username) {
        projectManagerService.leaveAllProjectsUnderUser(username); //TODO: do in Transactional
        userService.deleteByUsername(username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
