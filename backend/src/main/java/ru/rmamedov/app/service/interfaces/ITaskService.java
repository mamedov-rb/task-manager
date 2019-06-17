package ru.rmamedov.app.service.interfaces;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.app.exception.TaskAlreadyExistsException;
import ru.rmamedov.app.exception.TaskNotFoundException;
import ru.rmamedov.app.model.Task;

import java.util.List;

/**
 * @author Rustam Mamedov
 */

public interface ITaskService extends IBaseService<Task> {

    void assignToUser(@NotNull final String taskId, @NotNull final String username) throws TaskAlreadyExistsException;

    @NotNull
    @Override
    Task findById(@NotNull final String id);

    @NotNull
    Task findByIdWithEagerProject(@NotNull final String id);

    @NotNull
    Task findByIdWithEagerUser(@NotNull final String id);

    @NotNull
    @Override
    List<Task> findAll() throws TaskNotFoundException;

    @NotNull
    @Override
    @Transactional
    Task save(@NotNull final Task task);
    @NotNull
    Task saveWithSelfInjection(@NotNull final Task task) throws TaskAlreadyExistsException;

    @NotNull
    @Transactional
    Task saveUnderUserAndProject(@NotNull final Task task, @NotNull final String projectId, @NotNull String username);

    @NotNull
    Task saveWithSelfInjectionUnderUserAndProject(@NotNull final Task task, @NotNull final String projectId, @NotNull String username) throws TaskAlreadyExistsException;



    @Transactional
    void deleteById(@NotNull final String id);

    @Override
    @Transactional
    void deleteAll();

    @NotNull
    @Override
    @Transactional
    Task update(@NotNull final Task task);

    @NotNull
    @Override
    @Transactional
    Task patch(@NotNull final Task task);

    @NotNull
    List<Task> findAllByProjectSortedByCreatedAsc(@NotNull final String id) throws TaskNotFoundException;

    @NotNull
    List<Task> findAllByProjectSortByStartDateAsc(@NotNull final String id) throws TaskNotFoundException;

    @NotNull
    List<Task> findAllByProjectSortByEndDateAsc(@NotNull final String id) throws TaskNotFoundException;

    @NotNull
    List<Task> findAllByProjectId(@NotNull final String id) throws TaskNotFoundException;
}

