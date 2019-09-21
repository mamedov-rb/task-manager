package ru.rmamedov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.rmamedov.taskmanager.exception.TaskNotFoundException;
import ru.rmamedov.taskmanager.model.Task;
import ru.rmamedov.taskmanager.repository.TaskRepository;

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
    public Task findById(final String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id: " + id + " - Not found!"));
    }

    public void deleteById(final String id) {
        final Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id: " + id + " - Not found!"));
        taskRepository.delete(task);
    }

}
