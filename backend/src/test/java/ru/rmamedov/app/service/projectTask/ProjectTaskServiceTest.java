package ru.rmamedov.app.service.projectTask;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.rmamedov.app.StartApp;
import ru.rmamedov.app.exception.TaskNotFoundException;
import ru.rmamedov.app.model.Project;
import ru.rmamedov.app.model.Task;
import ru.rmamedov.app.service.interfaces.IProjectService;
import ru.rmamedov.app.service.interfaces.ITaskService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Rustam Mamedov
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StartApp.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProjectTaskServiceTest {

    @Autowired
    private IProjectService projectService;

    @Autowired
    private ITaskService taskService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private String projectId1 = null;
    private String projectId2 = null;
    private String taskId1 = null;
    private String taskId2 = null;

    @Before
    public void init() {
        projectId1 = projectService
                .saveWithSelfInjection(
                        new Project("Project_1",
                                "Some description of project #1.",
                                LocalDate.parse("2019-07-10"),
                                LocalDate.parse("2020-10-10"))).getId();
        projectId2 = projectService
                .saveWithSelfInjection(
                        new Project("Project_2",
                                "Some description of project #2.",
                                LocalDate.parse("2019-07-01"),
                                LocalDate.parse("2020-10-10"))).getId();
        taskId1 = taskService
                .saveWithSelfInjection(
                        new Task("Task_1", "Some task description of task #1",
                                LocalDate.parse("2019-07-15"), LocalDate.parse("2019-08-25"))).getId();
        taskId2 = taskService
                .saveWithSelfInjection(
                        new Task("Task_2", "Some task description of task #2",
                                LocalDate.parse("2019-08-16"), LocalDate.parse("2019-09-26"))).getId();
    }

    @After
    public void destroy() {
        taskService.deleteAll();
        projectService.deleteAll();
    }


    @Test
    public void servicesExistsTest() {
        assertNotNull(projectService);
        assertNotNull(taskService);
    }

    @Test
    @DirtiesContext
    public void findAllTasksOfProjectTest() {
        projectService.addTaskAndUpdate(projectId1, taskId1);
        projectService.addTaskAndUpdate(projectId1, taskId2);
        final Project project = projectService.findByIdWithEagerTasks(projectId1);
        assertEquals(2, project.getTasks().size());
    }

    @Test
    @DirtiesContext
    public void findAllProjectsWithTasksSortedByStartDateTest() {
        projectService.addTaskAndUpdate(projectId1, taskId1);
        projectService.addTaskAndUpdate(projectId1, taskId2);
        final List<Project> projects = projectService.findAllWithEagerTasksSortByStartDate();

        assertEquals(2, projects.size());

        final Project project2 = projects.get(0);
        final Project project1 = projects.get(1);
        final Set<Task> tasks = project1.getTasks();

        assertEquals(LocalDate.parse("2019-07-01"), project2.getStartDate());
        assertEquals("Project_2", project2.getName());
        assertEquals(LocalDate.parse("2019-07-10"), project1.getStartDate());
        assertEquals("Project_1", project1.getName());

        assertEquals(2, tasks.size());
    }

    @Test
    @DirtiesContext
    public void removeProjectWithTasksCascadeTest() {
        exceptionRule.expect(TaskNotFoundException.class);
        exceptionRule.expectMessage("There are is no any tasks!");

        final Project project = projectService.findByIdWithEagerTasks(projectId1);
        projectService.addTaskAndUpdate(projectId1, taskId1);
        projectService.addTaskAndUpdate(projectId1, taskId2);

        projectService.deleteById(project.getId());
        assertEquals(1, projectService.findAll().size());
        taskService.findAll();
    }

    @Test
    @DirtiesContext
    public void addTaskToProjectTest() {
        final Task task = taskService.saveWithSelfInjection(
                new Task("Task_3", "Some task description of task #3",
                LocalDate.parse("2019-09-17"), LocalDate.parse("2019-09-26")));
        projectService.addTaskAndUpdate(projectId1, taskId1);
        projectService.addTaskAndUpdate(projectId1, taskId2);
        projectService.addTaskAndUpdate(projectId1, task.getId());

        final Project project = projectService.findByIdWithEagerTasks(projectId1);

        final Set<Task> tasks = project.getTasks();
        assertEquals(3, tasks.size());
        assertTrue(tasks.contains(task));
    }
    @Test
    @DirtiesContext
    public void deleteTaskAlsoFromProjectTest() {
        projectService.addTaskAndUpdate(projectId1, taskId1);
        projectService.addTaskAndUpdate(projectId1, taskId2);
        projectService.removeTaskAndUpdate(projectId1, taskId1);

        final Project projectAfterRemove = projectService.findByIdWithEagerTasks(projectId1);
        assertEquals(1, projectAfterRemove.getTasks().size());
        assertEquals(1, taskService.findAll().size());
    }

    @Test
    @DirtiesContext
    public void removeTaskFromProjectWhileProjectHasNoSuchTaskTest() {
        exceptionRule.expect(TaskNotFoundException.class);
        exceptionRule.expectMessage("Project with ID: '" + projectId1 + "' - Has no Task with ID: '" + taskId1 + "'!");

        projectService.removeTaskAndUpdate(projectId1, taskId1);
    }

    @Test
    @DirtiesContext
    public void updateTaskWhileChangedProjectTest() {
        projectService.addTaskAndUpdate(projectId1, taskId1);
        projectService.addTaskAndUpdate(projectId1, taskId2);
        projectService.addTaskAndUpdate(projectId2, taskId1);

        final Task task1 = taskService.findByIdWithEagerProject(taskId1);
        final Task task2 = taskService.findByIdWithEagerProject(taskId2);
        final Project project1 = projectService.findByIdWithEagerTasks(projectId1);
        final Project project2 = projectService.findByIdWithEagerTasks(projectId2);

        assertEquals("Project_2", task1.getProject().getName());
        assertFalse(project1.getTasks().contains(task1));
        assertTrue(project1.getTasks().contains(task2));
        assertTrue(project2.getTasks().contains(task1));
        assertFalse(project2.getTasks().contains(task2));
    }
}