package ru.rmamedov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.taskmanager.exception.UserNotAuthorizedException;
import ru.rmamedov.taskmanager.model.Comment;
import ru.rmamedov.taskmanager.model.DTO.SaveCommentRequest;
import ru.rmamedov.taskmanager.model.DTO.SaveTaskRequest;
import ru.rmamedov.taskmanager.model.Project;
import ru.rmamedov.taskmanager.model.Task;
import ru.rmamedov.taskmanager.model.User;

import javax.validation.Valid;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Rustam Mamedov
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectManagerService {

    private final UserService userService;

    private final ProjectService projectService;

    private final TaskService taskService;

    private final CommentService commentService;

    @Transactional
    public boolean assignUserToProject(final String username, final String projectId) {
        @NotNull final User user = userService.findByUsernameWithEagerProject(username);
        @NotNull final Project project = projectService.findByIdWithEagerUsers(projectId);
        return user.addProject(project);
    }

    @Transactional
    public boolean leaveProjectUnderUser(final String username, final String projectId) {
        @NotNull final User user = userService.findByUsernameWithEagerProject(username);
        @NotNull final Project project = projectService.findByIdWithEagerUsers(projectId);
        final Set<Task> tasks = taskService.findAllByAssignedToAndProject(user, project);
        if (!tasks.isEmpty()) {
            final long removed = taskService.deleteAllByAssignedToAndProject(user, project);
            log.info("While user '{}' were leaving project '{}', he removed: '{}' tasks.", user.getUsername(), project.getName(), removed);
        }
        return user.removeProject(project);
    }

    @Transactional
    public void leaveAllProjectsUnderUser(final String username) {
        @NotNull final User user = userService.findByUsernameWithEagerProject(username);
        final Iterator<Project> iterator = user.getProjects().iterator();
        while (iterator.hasNext()) {
            final Project project = iterator.next();
            final Set<Task> tasks = taskService.findAllByAssignedToAndProject(user, project);
            if (!tasks.isEmpty()) {
                final long removed = taskService.deleteAllByAssignedToAndProject(user, project);
                log.info("While user '{}' were leaving project '{}', he removed: '{}' tasks.", user.getUsername(), project.getName(), removed);
            }
            project.removeUser(user);
            iterator.remove();
        }
    }

    @Transactional
    public boolean createAndAssignTaskToUser(@NotNull final SaveTaskRequest request,
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

    @Transactional
    public boolean saveCommentUnderUserAndTask(final SaveCommentRequest request, final Authentication authentication) {
        if (authentication == null) {
            throw new UserNotAuthorizedException("User - Not authorized. Please login");
        }
        final User user = (User) userService.loadUserByUsername(authentication.getName());
        @Valid final Comment comment = request.getComment();
        comment.setCommentator(user);
        final Task task = taskService.findByIdWithEagerComments(request.getTaskId());
        return task.addComment(comment);
    }

    @Transactional
    public boolean removeCommentUnderUserAndTask(final String id) {
        final Comment comment = commentService.findByIdWithEagerCommentatorAndTask(id);
        final Task task = taskService.findByCommentWithEagerComments(comment);
        return task.removeComment(comment);
    }

}
