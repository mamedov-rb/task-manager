package ru.rmamedov.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.rmamedov.taskmanager.model.DTO.SaveCommentRequest;
import ru.rmamedov.taskmanager.model.DTO.SaveTaskRequest;
import ru.rmamedov.taskmanager.model.DTO.UserPreviewDTO;
import ru.rmamedov.taskmanager.model.Project;
import ru.rmamedov.taskmanager.service.ProjectManagerService;
import ru.rmamedov.taskmanager.service.ProjectService;
import ru.rmamedov.taskmanager.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, List<?>> globalSearch(@NotNull @RequestParam(value = "param", required = false, defaultValue = "") final String param) {
        return projectManagerService.globalSearch(param);
    }

    @PostMapping(value = "/project/save", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity create(@RequestBody @Valid final Project project,
                                 @Nullable @AuthenticationPrincipal Authentication authentication) {
        final boolean created = projectManagerService.saveProjectAndAssignUserToIt(project, authentication);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @GetMapping(value = "/users/{projectId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<UserPreviewDTO>> findAllByProject(@PathVariable final String projectId) {
        return ResponseEntity.status(HttpStatus.OK).body(projectManagerService.findAllByProjectWithRoles(projectId));
    }

    @PatchMapping("/assign/username/{username}/projectId/{id}")
    public ResponseEntity assignUserToProject(@PathVariable final String username, @PathVariable String id) {
        final boolean assigned = projectManagerService.assignUserToProject(username, id);
        if (assigned) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @PatchMapping("/assign/yourself/projectId/{id}")
    public ResponseEntity assignYourselfToProject(@PathVariable String id,
                                                  @AuthenticationPrincipal Authentication authentication) {

        final boolean assigned = projectManagerService.assignYourselfToProject(id, authentication);
        if (assigned) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }

    @PatchMapping("/leave/projectId/{id}")
    public ResponseEntity leaveProjectWithTasksUnderUser(@PathVariable String id,
                                                         @AuthenticationPrincipal Authentication authentication) {

        final boolean assigned = projectManagerService.leaveProjectUnderUser(id, authentication);
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
    public ResponseEntity createTaskAndAssignToYourself(@Valid @RequestBody SaveTaskRequest request,
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

    @DeleteMapping("/delete/project/{id}")
    public ResponseEntity deleteProjectWithTasksUnderUser(@PathVariable String id,
                                                          @AuthenticationPrincipal Authentication authentication) {

        projectManagerService.leaveProjectUnderUser(id, authentication); //TODO: do in Transactional
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
