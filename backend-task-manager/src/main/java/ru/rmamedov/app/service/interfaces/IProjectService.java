package ru.rmamedov.app.service.interfaces;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.app.exception.ProjectAlreadyExistsException;
import ru.rmamedov.app.exception.ProjectNotFoundException;
import ru.rmamedov.app.exception.TaskAlreadyExistsException;
import ru.rmamedov.app.exception.TaskNotFoundException;
import ru.rmamedov.app.model.Project;

import java.util.List;

/**
 * @author Rustam Mamedov
 */

public interface IProjectService extends IBaseService<Project> {

    void addTaskAndUpdate(@NotNull final String projectId, @NotNull final String taskId) throws TaskAlreadyExistsException;

    void removeTaskAndUpdate(@NotNull final String projectId, @NotNull final String taskId) throws TaskNotFoundException;

    @NotNull
    @Override
    Project findById(@NotNull final String id);

    @NotNull
    Project findByIdWithEagerTasks(@NotNull final String id);

    @NotNull
    Project findByIdWithEagerUsers(@NotNull final String id);

    @NotNull
    @Override
    List<Project> findAll();

    @NotNull
    @Transactional
    List<Project> findAllOfCurrentUser(@NotNull final String id) throws ProjectNotFoundException;

    @NotNull
    @Override
    @Transactional
    Project save(@NotNull final Project project);
    @NotNull
    Project saveWithSelfInjection(@NotNull final Project project) throws ProjectAlreadyExistsException;

    @Override
    @Transactional
    void deleteById(@NotNull final String id);

    @Override
    @Transactional
    void deleteAll();

    @NotNull
    @Override
    @Transactional
    Project update(@NotNull final Project project);

    @NotNull
    @Override
    @Transactional
    Project patch(@NotNull final Project project);

    @NotNull
    List<Project> findAllWithEagerTasksSortByCreationDate();

    @NotNull
    List<Project> findAllWithEagerTasksSortByStartDate();

    @NotNull
    List<Project> findAllWithEagerTasksSortByEndDate();

    @NotNull
    List<Project> searchAnyByNameOrDesc(@NotNull final String string);
}
