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
        final User user = userService.findByUsernameWithEagerProject(username);
        final Project project = projectService.findByIdWithEagerUsers(projectId);
        return user.addProject(project);
    }

    @Transactional
    public boolean createAndAssignTaskToUser(@NotNull final CreationTaskRequest request,
                                          @Nullable final Authentication authentication) {

        if (authentication == null) {
            throw new UserNotAuthorizedException("User - Not authorized. Please login");
        }
        @NotNull
        final User createdBy = (User) userService.loadUserByUsername(authentication.getName());
        @NotNull
        final User assignTo = (User) userService.loadUserByUsername(request.getAssignTo());
        @NotNull
        final Project project = projectService.findById(request.getProjectId());
        @NotNull
        final Task task = request.getTask();

        task.setProject(project);
        task.setCreatedBy(createdBy);
        task.setAssignedTo(assignTo);

        return taskService.save(task).getId() != null;
    }

}
