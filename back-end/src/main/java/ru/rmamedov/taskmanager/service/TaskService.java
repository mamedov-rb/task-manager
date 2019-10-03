package ru.rmamedov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.taskmanager.exception.TaskNotFoundException;
import ru.rmamedov.taskmanager.exception.UserNotAuthorizedException;
import ru.rmamedov.taskmanager.model.Comment;
import ru.rmamedov.taskmanager.model.DTO.TaskProjection;
import ru.rmamedov.taskmanager.model.Project;
import ru.rmamedov.taskmanager.model.Task;
import ru.rmamedov.taskmanager.model.User;
import ru.rmamedov.taskmanager.repository.TaskRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task save(final Task task) {
        return taskRepository.save(task);
    }

    @NotNull
    public Task findByIdWithEagerAssignedTo(final String id) {
        return taskRepository.findByIdWithEagerAssignedTo(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id: " + id + " - Not found!"));
    }

    @NotNull
    public Task findByCommentWithEagerComments(final Comment comment) {
        return taskRepository.findByCommentWithEagerComments(comment)
                .orElseThrow(() -> new TaskNotFoundException("Task with id: " + comment.getId() + " - Not found!"));
    }

    @NotNull
    public Task findByIdWithEagerComments(final String id) {
        return taskRepository.findByIdWithEagerAssignedTo(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id: " + id + " - Not found!"));
    }

    @NotNull
    public Set<Task> findAllByAssignedToAndProject(final User user, final Project project) {
        return taskRepository.findAllByAssignedToAndProject(user, project);
    }

    @NotNull
    public Set<TaskProjection> findAllByAssignedToAndProjectAsDTO(final Authentication authentication, final String projectId) {
        if (authentication == null) {
            throw new UserNotAuthorizedException("User - Not authorized. Please login");
        }
        return taskRepository.findAllByProjectIdAndAssignedToUsernameOrCreatedByUsername(projectId, authentication.getName(), authentication.getName());
    }

    public long deleteAllByAssignedToAndProject(final User user, final Project project) {
        return taskRepository.deleteAllByAssignedToAndProject(user, project);
    }

    @Transactional
    public void deleteById(final String id) {
        final Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id: " + id + " - Not found!"));
        taskRepository.delete(task);
    }


}
