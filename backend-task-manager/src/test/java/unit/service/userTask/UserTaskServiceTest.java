package unit.service.userTask;

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
import ru.rmamedov.app.config.PersistentConfig;
import ru.rmamedov.app.exception.TaskAlreadyExistsException;
import ru.rmamedov.app.model.Task;
import ru.rmamedov.app.model.user.User;
import ru.rmamedov.app.service.interfaces.ITaskService;
import ru.rmamedov.app.service.interfaces.IUserService;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Rustam Mamedov
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StartApp.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserTaskServiceTest {

    @Autowired
    private IUserService userService;

    @Autowired
    private ITaskService taskService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void userServiceIsExistsTest() {
        assertNotNull(userService);
    }

    @Test
    public void taskServiceIsExistsTest() {
        assertNotNull(taskService);
    }

    @Test
    @DirtiesContext
    public void assignTaskToUserThenReassignItToAnotherUserTest() {
        final String username1 = "albertEnstain";
        final String username2 = "johnTravolta";
        final String taskId = "taskID_1";

        taskService.assignToUser(taskId, username1);

        final User user1 = userService.findByUsernameWithEagerTasks(username1);
        final User user2 = userService.findByUsernameWithEagerTasks(username2);
        final Task task = taskService.findByIdWithEagerUser(taskId);
        final Set<Task> tasks1 = user1.getTasks();
        final Set<Task> tasks2 = user2.getTasks();

        assertEquals(1, tasks1.size());
        assertEquals(0, tasks2.size());
        assertTrue(tasks1.contains(task));
        assertFalse(tasks2.contains(task));
        assertEquals("albertEnstain", task.getUser().getUsername());

        taskService.assignToUser(taskId, username2);

        final User user1AfterAssign = userService.findByUsernameWithEagerTasks(username1);
        final User user2AfterAssign = userService.findByUsernameWithEagerTasks(username2);
        final Task taskAfterAssign = taskService.findByIdWithEagerUser(taskId);
        final Set<Task> tasks1AfterAssign = user1AfterAssign.getTasks();
        final Set<Task> tasks2AfterAssign = user2AfterAssign.getTasks();

        assertEquals(0, tasks1AfterAssign.size());
        assertEquals(1, tasks2AfterAssign.size());
        assertFalse(tasks1AfterAssign.contains(taskAfterAssign));
        assertTrue(tasks2AfterAssign.contains(taskAfterAssign));
        assertEquals("johnTravolta", taskAfterAssign.getUser().getUsername());
    }

    @Test
    @DirtiesContext
    public void assignTaskToUserWileHeAlreadyHadItTest() {
        exceptionRule.expect(TaskAlreadyExistsException.class);
        exceptionRule.expectMessage("User with username: 'albertEnstain' - Already has Task with ID: 'taskID_1'!");

        final String taskId = "taskID_1";
        final String username = "albertEnstain";

        taskService.assignToUser(taskId, username);
        taskService.assignToUser(taskId, username);
    }

    @Test
    @DirtiesContext
    public void whenRemoveTaskWhileItBelongToUserThenSuccessTest() {
        final String taskId = "taskID_1";
        final String username = "albertEnstain";

        taskService.assignToUser(taskId, username);
        taskService.deleteById(taskId);

        final Set<Task> tasks = userService.findByUsernameWithEagerTasks(username).getTasks();
        assertEquals(0, tasks.size());
    }
}
