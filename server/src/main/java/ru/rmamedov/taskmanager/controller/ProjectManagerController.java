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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rmamedov.taskmanager.model.DTO.SaveCommentRequest;
import ru.rmamedov.taskmanager.model.DTO.SaveTaskRequest;
import ru.rmamedov.taskmanager.model.DTO.UserMetaDTO;
import ru.rmamedov.taskmanager.model.Project;
import ru.rmamedov.taskmanager.service.ProjectManagerService;
import ru.rmamedov.taskmanager.service.ProjectService;
import ru.rmamedov.taskmanager.service.UserService;

import javax.validation.Valid;
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
    public ResponseEntity<Set<UserMetaDTO>> findAllByProject(@PathVariable final String projectId) {
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

    @PatchMapping("/assign/yourself/projectId/{id}") // TODO: 2019-10-03 join this with assign-by-username controller with optional request param.
    public ResponseEntity assignYourselfToProject(@PathVariable String id, @AuthenticationPrincipal Authentication authentication) {
        final boolean assigned = projectManagerService.assignYourselfToProject(id, authentication);
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
