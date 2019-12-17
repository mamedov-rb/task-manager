package ru.rmamedov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.taskmanager.exception.TaskNotFoundException;
import ru.rmamedov.taskmanager.model.Comment;
import ru.rmamedov.taskmanager.model.DTO.TaskDetailsProjection;
import ru.rmamedov.taskmanager.model.DTO.TaskPreviewProjection;
import ru.rmamedov.taskmanager.model.DTO.TaskProjection;
import ru.rmamedov.taskmanager.model.Project;
import ru.rmamedov.taskmanager.model.Task;
import ru.rmamedov.taskmanager.model.User;
import ru.rmamedov.taskmanager.model.enums.Status;
import ru.rmamedov.taskmanager.repository.TaskRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    List<TaskPreviewProjection> getPreview(final String param, Pageable pageable) {
        return taskRepository.getPreview(param, pageable);
    }

    public Task save(final Task task) {
        return taskRepository.save(task);
    }

    @NotNull
    public Task findByIdWithEagerAssignedTo(final String id) {
        return taskRepository.findByIdWithEagerAssignedTo(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id: " + id + " - Not found!"));
    }

    @NotNull
    public TaskDetailsProjection getDetails(final String id) {
        return taskRepository.getDetails(id)
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
    public Set<TaskProjection> findAllByAssignedToAndProjectAsDTO(final String projectId) {
        return taskRepository.findAllByProject(projectId);
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

    @Transactional
    public void setStatus(final String taskId, final Status status) {
        final Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with id: " + taskId + " - Not found!"));
        task.setStatus(status);
    }
}
