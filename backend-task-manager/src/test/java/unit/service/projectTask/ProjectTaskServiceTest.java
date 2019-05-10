package unit.service.projectTask;

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
import ru.rmamedov.app.model.Status;
import ru.rmamedov.app.model.Task;
import ru.rmamedov.app.service.interfaces.IProjectService;
import ru.rmamedov.app.service.interfaces.ITaskService;

import java.time.LocalDate;
import java.time.Month;
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

    @Test
    public void projectServiceIsExistsTest() {
        assertNotNull(projectService);
    }

    @Test
    public void taskServiceIsExistsTest() {
        assertNotNull(taskService);
    }

    @Test
    public void findAllTasksOfProjectTest() {
        final Project project = projectService.findByIdWithEagerTasks("projectID_1");
        assertEquals(2, project.getTasks().size());
    }

    @Test
    public void findAllProjectsWithTasksSortedByStartDateTest() {
        final List<Project> projects = projectService.findAllWithEagerTasksSortByStartDate();

        assertEquals(2, projects.size());

        final Project project1 = projects.get(0);
        final Project project2 = projects.get(1);

        assertEquals(LocalDate.of(2019, Month.MARCH, 1), project1.getStartDate());
        assertEquals(LocalDate.of(2019, Month.JULY, 10), project2.getStartDate());
        assertEquals(2, project2.getTasks().size());
    }

    @Test
    @DirtiesContext
    public void createProjectWithTaskTest() {
        final Task task = taskService.save(new Task("Task_3", "Some task description of task #3",
                Status.PLANNED, LocalDate.of(2019, Month.AUGUST, 16), LocalDate.of(2019, Month.SEPTEMBER, 26)));
        final Project project = projectService.save(new Project("Project_3", "Some description of project #3.",
                Status.PLANNED, LocalDate.of(2019, Month.JULY, 10), LocalDate.of(2020, Month.OCTOBER, 10)));

        projectService.addTaskAndUpdate(project.getId(), task.getId());

        final Project updatedProject = projectService.findByIdWithEagerTasks(project.getId());
        final Task savedTask = taskService.findByIdWithEagerProject(task.getId());

        assertEquals("Project_3", updatedProject.getName());
        assertEquals("Some description of project #3.", updatedProject.getDescription());
        assertEquals(1, updatedProject.getTasks().size());
        assertTrue(updatedProject.getTasks().contains(savedTask));
        assertEquals("Task_3", savedTask.getName());
        assertEquals("Project_3", savedTask.getProject().getName());
    }

    @Test
    @DirtiesContext
    public void removeProjectWithTasksCascadeTest() {
        exceptionRule.expect(TaskNotFoundException.class);
        final Project project = projectService.findByIdWithEagerTasks("projectID_1");

        assertEquals(2, taskService.findAll().size());
        assertEquals(2, project.getTasks().size());

        projectService.deleteById(project.getId());
        taskService.findAll().size();
    }

    @Test
    @DirtiesContext
    public void addTaskToProjectTest() {
        final Task task = taskService.save(new Task( "Task_3", "Some task description of task #3",
                Status.PLANNED, LocalDate.of(2019, Month.AUGUST, 16), LocalDate.of(2019, Month.SEPTEMBER, 26)));
        final Project project = projectService.findByIdWithEagerTasks("projectID_1");

        projectService.addTaskAndUpdate(project.getId(), task.getId());

        final Project updatedProject = projectService.findByIdWithEagerTasks("projectID_1");
        final Set<Task> tasks = updatedProject.getTasks();
        assertEquals(3, tasks.size());
        assertTrue(tasks.contains(task));
    }
    @Test
    @DirtiesContext
    public void removeTaskFromProjectTest() {
        final String taskId = "taskID_1";
        final String projectId = "projectID_1";

        projectService.removeTaskAndUpdate(projectId, taskId);

        final Project projectAfterRemove = projectService.findByIdWithEagerTasks("projectID_1");
        assertEquals(1, projectAfterRemove.getTasks().size());
        assertEquals(1, taskService.findAll().size());
    }

    @Test
    @DirtiesContext
    public void removeTaskFromProjectWhileProjectHasNoSuchTaskTest() {
        exceptionRule.expect(TaskNotFoundException.class);
        exceptionRule.expectMessage("Project with ID: 'projectID_2' - Has no Task with ID: 'taskID_1'!");
        final String taskId = "taskID_1";
        final String projectId = "projectID_2";

        projectService.removeTaskAndUpdate(projectId, taskId);
    }

    @Test
    @DirtiesContext
    public void updateTaskWhileChangedProjectTest() {
        final Task task = taskService.findByIdWithEagerProject("taskID_1");
        final Project project1 = projectService.findByIdWithEagerTasks("projectID_1");
        final Project project2 = projectService.findByIdWithEagerTasks("projectID_2");

        assertTrue(project1.getTasks().contains(task));
        assertEquals(2, project1.getTasks().size());

        projectService.addTaskAndUpdate(project2.getId(), task.getId());

        final Task task1updated = taskService.findByIdWithEagerProject("taskID_1");
        final Project project1updated = projectService.findByIdWithEagerTasks("projectID_1");
        final Project project2updated = projectService.findByIdWithEagerTasks("projectID_2");

        assertEquals("Project_2", task1updated.getProject().getName());
        assertFalse(project1updated.getTasks().contains(task1updated));
        assertTrue(project2updated.getTasks().contains(task1updated));
    }
}