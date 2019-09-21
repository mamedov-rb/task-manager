package ru.rmamedov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.taskmanager.exception.UserNotAuthorizedException;
import ru.rmamedov.taskmanager.model.DTO.CreationTaskRequest;
import ru.rmamedov.taskmanager.model.Project;
import ru.rmamedov.taskmanager.model.Task;
import ru.rmamedov.taskmanager.model.User;

/**
 * @author Rustam Mamedov
 */

@Service
@RequiredArgsConstructor
public class ProjectManagerService {

    private final UserService userService;

    private final ProjectService projectService;

    private final TaskService taskService;

    @Transactional
    public boolean assignUserToProject(final String username, final String projectId) {
        @NotNull final User user = userService.findByUsernameWithEagerProject(username);
        @NotNull final Project project = projectService.findByIdWithEagerUsers(projectId);
        return user.addProject(project);
    }

    @Transactional
    public boolean createAndAssignTaskToUser(@NotNull final CreationTaskRequest request,
                                          @Nullable final Authentication authentication) {

        if (authentication == null) {
            throw new UserNotAuthorizedException("User - Not authorized. Please login");
        }
        @NotNull final Project project = projectService.findById(request.getProjectId());

        @NotNull final User createdBy = (User) userService.loadUserByUsername(authentication.getName());

        @NotNull final User user = userService.findByUsernameAndProject(request.getAssignTo(), project);

        @NotNull final Task task = request.getTask();

        task.setProject(project);
        task.setCreatedBy(createdBy);
        task.setAssignedTo(user);

        return taskService.save(task).getId() != null;
    }

    @Transactional
    public void reassignTaskToAnotherUser(final String taskId,
                                             final String username,
                                             final String projectId) {

        @NotNull final Project project = projectService.findById(projectId);

        @NotNull final User user = userService.findByUsernameAndProject(username, project);

        @NotNull final Task task = taskService.findByIdWithEagerAssignedTo(taskId);

        task.setAssignedTo(user);
    }

}
