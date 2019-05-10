package unit.service;

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
import ru.rmamedov.app.exception.TaskAlreadyExistsException;
import ru.rmamedov.app.exception.TaskNotFoundException;
import ru.rmamedov.app.model.Status;
import ru.rmamedov.app.model.Task;
import ru.rmamedov.app.service.interfaces.ITaskService;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Rustam Mamedov
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StartApp.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class TaskServiceTest {

    @Autowired
    private ITaskService taskService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void taskServiceIsExistsTest() {
        assertNotNull(taskService);
    }

    @Test
    @DirtiesContext
    public void saveUniqueTaskTest() {
        assertEquals(2, taskService.findAll().size());
        final Task task = taskService.save(new Task("Task_3", "Some task description of task #3",
                Status.PLANNED, LocalDate.of(2019, Month.AUGUST, 16), LocalDate.of(2019, Month.SEPTEMBER, 26)));
        assertEquals("Task_3", task.getName());
        assertEquals("Some task description of task #3", task.getDescription());
        assertNotNull(task.getCreated());
        assertNotNull(task.getUpdated());
        assertEquals(3, taskService.findAll().size());
    }
    @Test
    @DirtiesContext
    public void saveTaskWithTheSameNameTest() {
        exceptionRule.expect(TaskAlreadyExistsException.class);
        exceptionRule.expectMessage("Task with name: 'Task_2' - Not saved!");
        taskService.saveWithSelfInjection(new Task("Task_2", "Some task description of task #2",
                Status.PLANNED, LocalDate.of(2019, Month.AUGUST, 16), LocalDate.of(2019, Month.SEPTEMBER, 26)));
    }

    @Test
    @DirtiesContext
    public void deleteTaskTest() {
        assertEquals(2, taskService.findAll().size());
        taskService.deleteById("taskID_1");
        assertEquals(1, taskService.findAll().size());
    }
    @Test
    @DirtiesContext
    public void deleteAllTaskTest() {
        exceptionRule.expect(TaskNotFoundException.class);
        exceptionRule.expectMessage("There are is no any tasks!");
        taskService.deleteAll();
        taskService.findAll();
    }

    @Test
    @DirtiesContext
    public void updateTaskTest() {
        final Task task = taskService.findById("taskID_1");
        task.setName("Updated task's name.");
        final Task updated = taskService.update(task);
        assertEquals("Updated task's name.", updated.getName());
    }
    @Test
    @DirtiesContext
    public void fetchTaskTest() {
        final Task task = taskService.findById("taskID_1");
        task.setName("Updated task's name.");
        final Task updated = taskService.patch(task);
        assertEquals("Updated task's name.", updated.getName());
    }

    @Test
    public void findOneTaskByIdTest() {
        final Task task = taskService.findById("taskID_2");
        assertEquals("Task_2", task.getName());
    }

    @Test
    public void findNonexistentTaskByIdTest() {
        exceptionRule.expect(TaskNotFoundException.class);
        exceptionRule.expectMessage("Task with ID: 'taskID_3' - Not found!");
        taskService.findById("taskID_3");
    }

    @Test
    public void findAllTasksTest() {
        assertEquals(2, taskService.findAll().size());
    }

    @Test
    public void findAllTasksWhenThereAreIsNothingTest() {
        exceptionRule.expect(TaskNotFoundException.class);
        exceptionRule.expectMessage("There are is no any tasks!");
        taskService.deleteAll();
        taskService.findAll();
    }
}
