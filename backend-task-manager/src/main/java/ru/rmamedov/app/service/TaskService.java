package ru.rmamedov.app.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.app.exception.TaskAlreadyExistsException;
import ru.rmamedov.app.exception.TaskNotFoundException;
import ru.rmamedov.app.model.Task;
import ru.rmamedov.app.model.user.User;
import ru.rmamedov.app.repository.ITaskRepository;
import ru.rmamedov.app.service.interfaces.ITaskService;
import ru.rmamedov.app.service.interfaces.IUserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskService implements ITaskService {

    private ITaskRepository taskRepository;
    private IUserService userService;

    @Autowired
    private ITaskService taskService;

    @Autowired
    public TaskService(ITaskRepository taskRepository, IUserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void assignToUser(@NotNull final String taskId, @NotNull final String username) throws TaskAlreadyExistsException {
        final Task task = taskService.findByIdWithEagerUser(taskId);
        final User user = userService.findByUsernameWithEagerTasks(username);
        final Set<Task> tasks = user.getTasks();
        if (tasks.contains(task)) {
            throw new TaskAlreadyExistsException("User with username: '" + user.getUsername() +
                    "' - Already has Task with ID: '" + taskId + "'!");
        }
        tasks.add(task);
        task.setUser(user);
        update(task);
    }

    @NotNull
    @Override
    @Transactional
    public Task save(@NotNull final Task task) {
        return taskRepository.saveAndFlush(task);
    }

    @NotNull
    @Override
    public Task saveWithSelfInjection(@NotNull final Task task) throws TaskAlreadyExistsException {
        try {
            return taskService.save(task);
        } catch (DataIntegrityViolationException ex) {
            throw new TaskAlreadyExistsException("Task with name: '" + task.getName() + "' - Not saved!");
        }
    }

    @Override
    @Transactional
    public void deleteById(@NotNull final String id) {
        taskRepository.delete(findById(id));
    }

    @Override
    @Transactional
    public void deleteAll() {
        taskRepository.deleteAll();
    }

    @NotNull
    @Override
    @Transactional
    public Task update(@NotNull Task task) {
        return taskRepository.saveAndFlush(task);
    }

    @NotNull
    @Override
    @Transactional
    public Task patch(@NotNull final Task updated) {
        final Task fromDb = taskService.findById(updated.getId());

        if (updated.getName() != null && !updated.getName().equals(fromDb.getName())) {
            fromDb.setName(updated.getName());
        }
        if (updated.getDescription() != null && !updated.getDescription().equals(fromDb.getDescription())) {
            fromDb.setDescription(updated.getDescription());
        }
        if (updated.getStartDate() != null && !updated.getStartDate().equals(fromDb.getStartDate())) {
            fromDb.setStartDate(updated.getStartDate());
        }
        if (updated.getEndDate() != null && !updated.getEndDate().equals(fromDb.getEndDate())) {
            fromDb.setEndDate(updated.getEndDate());
        }
        if (updated.getStatus() != null && !updated.getStatus().equals(fromDb.getStatus())) {
            fromDb.setStatus(updated.getStatus());
        }
        return taskRepository.saveAndFlush(fromDb);
    }

    @NotNull
    @Override
    public Task findById(@NotNull final String id) throws TaskNotFoundException {
        final Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            return optionalTask.get();
        }
        throw new TaskNotFoundException("Task with ID: '" + id + "' - Not found!");
    }

    @NotNull
    @Override
    public Task findByIdWithEagerProject(@NotNull String id) {
        final Optional<Task> optionalTask = taskRepository.findByIdWithEagerProject(id);
        if (optionalTask.isPresent()) {
            return optionalTask.get();
        }
        throw new TaskNotFoundException("Task with ID: '" + id + "' - Not found!");
    }

    @NotNull
    @Override
    public Task findByIdWithEagerUser(@NotNull String id) {
        final Optional<Task> optionalTask = taskRepository.findByIdWithEagerUser(id);
        if (optionalTask.isPresent()) {
            return optionalTask.get();
        }
        throw new TaskNotFoundException("Task with ID: '" + id + "' - Not found!");
    }

    @NotNull
    @Override
    public List<Task> findAll() throws TaskNotFoundException {
        final List<Task> tasks = taskRepository.findAll();
        if (tasks != null && !tasks.isEmpty()) {
            return Collections.unmodifiableList(tasks);
        }
        throw new TaskNotFoundException("There are is no any tasks!");
    }

    @NotNull
    @Override
    public List<Task> findAllByProjectSortedByCreatedAsc(@NotNull final String id) throws TaskNotFoundException {
        final List<Task> tasks = taskRepository.findAllByProjectOrderByCreatedAsc(id);
        if (tasks != null && !tasks.isEmpty()) {
            return Collections.unmodifiableList(tasks);
        }
        throw new TaskNotFoundException("There are is no any tasks!");
    }

    @NotNull
    @Override
    public List<Task> findAllByProjectSortByStartDateAsc(@NotNull final String id) throws TaskNotFoundException {
        final List<Task> tasks = taskRepository.findAllByProjectOrderByStartDateAsc(id);
        if (tasks != null && !tasks.isEmpty()) {
            return Collections.unmodifiableList(tasks);
        }
        throw new TaskNotFoundException("There are is no any tasks!");
    }

    @NotNull
    @Override
    public List<Task> findAllByProjectSortByEndDateAsc(@NotNull final String id) throws TaskNotFoundException {
        final List<Task> tasks = taskRepository.findAllByProjectOrderByEndDateAsc(id);
        if (tasks != null && !tasks.isEmpty()) {
            return Collections.unmodifiableList(tasks);
        }
        throw new TaskNotFoundException("There are is no any tasks!");
    }

    @NotNull
    @Override
    public List<Task> findAllByProjectId(@NotNull final String id) throws TaskNotFoundException {
        final List<Task> tasks = taskRepository.findAllByProjectId(id);
        if (tasks != null && !tasks.isEmpty()) {
            return Collections.unmodifiableList(tasks);
        }
        throw new TaskNotFoundException("There are is no any tasks!");
    }
}
