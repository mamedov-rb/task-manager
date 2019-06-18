package ru.rmamedov.app.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.app.exception.ProjectAlreadyExistsException;
import ru.rmamedov.app.exception.ProjectNotFoundException;
import ru.rmamedov.app.exception.TaskAlreadyExistsException;
import ru.rmamedov.app.exception.TaskNotFoundException;
import ru.rmamedov.app.model.Project;
import ru.rmamedov.app.model.Task;
import ru.rmamedov.app.repository.IProjectRepository;
import ru.rmamedov.app.service.interfaces.IProjectService;
import ru.rmamedov.app.service.interfaces.ITaskService;
import ru.rmamedov.app.service.interfaces.IUserService;

import java.util.*;

@Service
public class ProjectService implements IProjectService {

    private IProjectRepository projectRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private ITaskService taskService;

    @Autowired
    private IProjectService projectService;

    @Autowired
    public ProjectService(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @NotNull
    @Override
    @Transactional
    public Project save(@NotNull final Project project) {
        return projectRepository.saveAndFlush(project);
    }
    @NotNull
    public Project saveWithSelfInjection(@NotNull final Project project) throws ProjectAlreadyExistsException {
        try {
            return projectService.save(project);
        } catch (DataIntegrityViolationException ex) {
            throw new ProjectAlreadyExistsException("Project with name: '" + project.getName() + "' - already exists!");
        }
    }

    @NotNull
    @Override
    @Transactional
    public Project update(@NotNull Project project) {
        return projectRepository.saveAndFlush(project);
    }

    @NotNull
    @Override
    @Transactional
    public Project patch(@NotNull final Project updated) {
        final Project fromDb = findById(updated.getId());
        if (updated.getName() != null && !updated.getName().equals(fromDb.getName())) {
            fromDb.setName(updated.getName());
        }
        if (updated.getDescription() != null && !updated.getDescription().equals(fromDb.getDescription())) {
            fromDb.setDescription(updated.getDescription());
        }
        if (updated.getStatus() != null && !updated.getStatus().equals(fromDb.getStatus())) {
            fromDb.setStatus(updated.getStatus());
        }
        if (updated.getStartDate() != null && !updated.getStartDate().equals(fromDb.getStartDate())) {
            fromDb.setStartDate(updated.getStartDate());
        }
        if (updated.getEndDate() != null && !updated.getEndDate().equals(fromDb.getEndDate())) {
            fromDb.setEndDate(updated.getEndDate());
        }
        return projectRepository.saveAndFlush(fromDb);
    }

    @Override
    @Transactional
    public void deleteById(@NotNull String id) {
        projectRepository.deleteById(id);
    }

    @Override
    @Transactional
    public boolean deleteByIdUnderUser(@NotNull final String projectId, @NotNull final String username) {
        boolean updated = userService.removeProjectAndUpdate(projectId, username);
        if (updated) {
            projectRepository.delete(findById(projectId));
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void deleteAll() {
        projectRepository.deleteAll();
    }

    @Override
    @Transactional
    public void addTaskAndUpdate(@NotNull final String projectId, @NotNull final String taskId) throws TaskAlreadyExistsException {
        final Project project = projectService.findByIdWithEagerTasks(projectId);
        final Task task = taskService.findByIdWithEagerProject(taskId);
        final Set<Task> tasks = project.getTasks();
        if (tasks.contains(task)) {
            throw new TaskAlreadyExistsException("Project with ID: '" + project.getId() +
                    "' - Already has Task with ID: '" + task.getId() + "'!");
        }
        task.setProject(project);
        tasks.add(task);
        update(project);
    }

    @Override
    @Transactional
    public void removeTaskAndUpdate(@NotNull final String projectId, @NotNull final String taskId) throws TaskNotFoundException {
        final Project project = projectService.findByIdWithEagerTasks(projectId);
        final Task task = taskService.findByIdWithEagerProject(taskId);
        final Set<Task> tasks = project.getTasks();
        if (!tasks.contains(task)) {
            throw new TaskNotFoundException("Project with ID: '" + project.getId() + "' - Has no Task with ID: '" + task.getId() + "'!");
        }
        tasks.remove(task);
        update(project);
    }

    @NotNull
    @Override
    @Transactional(readOnly=true)
    public Project findById(@NotNull final String id) throws ProjectNotFoundException {
        final Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            return optionalProject.get();
        }
        throw new ProjectNotFoundException("Project with ID: '" + id + "' - Not found!");
    }

    @NotNull
    @Override
    @Transactional(readOnly=true)
    public Project findByIdWithEagerTasks(@NotNull final String id) throws ProjectNotFoundException {
        final Optional<Project> optionalProject = projectRepository.findByIdWithEagerTasks(id);
        if (optionalProject.isPresent()) {
            return optionalProject.get();
        }
        throw new ProjectNotFoundException("Project with ID: '" + id + "' - Not found!");
    }

    @NotNull
    @Override
    @Transactional(readOnly=true)
    public Project findByIdWithEagerUsers(@NotNull final String id) throws ProjectNotFoundException {
        final Optional<Project> optionalProject = projectRepository.findByIdWithEagerUsers(id);
        if (optionalProject.isPresent()) {
            return optionalProject.get();
        }
        throw new ProjectNotFoundException("Project with ID: '" + id + "' - Not found!");
    }

    @NotNull
    @Override
    @Transactional(readOnly=true)
    public List<Project> findAll() throws ProjectNotFoundException {
        final List<Project> projects = projectRepository.findAll();
        if (projects != null && !projects.isEmpty()) {
            return Collections.unmodifiableList(projects);
        }
        throw new ProjectNotFoundException("There is no any projects!");
    }

    @NotNull
    @Override
    @Transactional(readOnly=true)
    public List<Project> findAllOfCurrentUser(@NotNull final String userId) throws ProjectNotFoundException {
        final Optional<List<Project>> projects = projectRepository.findAllOfCurrentUser(userId);
        if (projects.isPresent()) {
            return Collections.unmodifiableList(projects.get());
        }
        throw new ProjectNotFoundException("There is no any projects!");
    }

    @NotNull
    @Override
    public List<Project> findAllWithEagerTasksSortByCreationDate() throws ProjectNotFoundException {
        final List<Project> projects = projectRepository.findAllSortByCreationDate();
        final List<Project> projectsWithTasks = new ArrayList<>();
        if (projects != null && !projects.isEmpty()) {
            for (final Project p : projects) {
                try {
                    final List<Task> tasks = taskService.findAllByProjectSortedByCreatedAsc(p.getId());
                    p.setTasks(new HashSet<>(tasks));
                } catch (TaskNotFoundException ex) {
                    p.setTasks(new HashSet<>());
                }
                projectsWithTasks.add(p);
            }
            return Collections.unmodifiableList(projectsWithTasks);
        }
        throw new ProjectNotFoundException("There is no any projects!");
    }

    @NotNull
    @Override
    public List<Project> findAllWithEagerTasksSortByStartDate() throws ProjectNotFoundException {
        final List<Project> projects = projectRepository.findAllSortByStartDate();
        final List<Project> projectsWithTasks = new ArrayList<>();
        if (projects != null && !projects.isEmpty()) {
            for (final Project p : projects) {
                try {
                    final List<Task> tasks = taskService.findAllByProjectSortByStartDateAsc(p.getId());
                    p.setTasks(new HashSet<>(tasks));
                } catch (TaskNotFoundException ex) {
                    p.setTasks(new HashSet<>());
                }
                projectsWithTasks.add(p);
            }
            return Collections.unmodifiableList(projectsWithTasks);
        }
        throw new ProjectNotFoundException("There is no any projects!");
    }

    @NotNull
    @Override
    public List<Project> findAllWithEagerTasksSortByEndDate() throws ProjectNotFoundException {
        final List<Project> list = projectRepository.findAllSortByEndDate();
        final List<Project> projectsWithTasks = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (final Project p : list) {
                try {
                    final List<Task> tasks = taskService.findAllByProjectSortByEndDateAsc(p.getId());
                    p.setTasks(new HashSet<>(tasks));
                } catch (TaskNotFoundException ex) {
                    p.setTasks(new HashSet<>());
                }
                projectsWithTasks.add(p);
            }
            return Collections.unmodifiableList(projectsWithTasks);
        }
        throw new ProjectNotFoundException("There is no any projects!");
    }

    @NotNull
    @Override
    public List<Project> searchAnyByNameOrDesc(@NotNull final String criteria) throws ProjectNotFoundException {
        final List<Project> list = projectRepository.findAnyByNameOrDesc(criteria);
        if (list != null && !list.isEmpty()) {
            return Collections.unmodifiableList(list);
        }
        throw new ProjectNotFoundException("There is no any projects!");
    }
}
