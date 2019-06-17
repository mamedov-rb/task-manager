package ru.rmamedov.app.service.userProjectTask;

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
import ru.rmamedov.app.exception.TaskAlreadyExistsException;
import ru.rmamedov.app.model.Project;
import ru.rmamedov.app.model.Task;
import ru.rmamedov.app.model.user.User;
import ru.rmamedov.app.service.interfaces.IProjectService;
import ru.rmamedov.app.service.interfaces.ITaskService;
import ru.rmamedov.app.service.interfaces.IUserService;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Rustam Mamedov
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StartApp.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserProjectTaskServiceTest {

    @Autowired
    private IUserService userService;

    @Autowired
    private IProjectService projectService;

    @Autowired
    private ITaskService taskService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private String projectId = null;
    private String taskId1 = null;
    private String taskId2 = null;

    @Before
    public void init() {
        userService
                .saveWithSelfInjection(new User("user_001", "123qwe", "John Travolta", 40,
                        "+7(900)800-70-07", "johnTravolta@mail.ru"));
        userService
                .saveWithSelfInjection(new User("user_002", "123qwe", "Albert Enstain", 30,
                        "+7(900)800-70-07", "albertEnstain@mail.ru"));
        projectId = projectService
                .saveWithSelfInjection(
                        new Project("Project_1",
                                "Some description of project #1.",
                                LocalDate.parse("2019-07-10"),
                                LocalDate.parse("2020-10-10"))).getId();
        taskId1 = taskService
                .saveWithSelfInjection(
                        new Task("Task_1", "Some task description of task #1",
                                LocalDate.parse("2019-07-15"), LocalDate.parse("2019-08-25"))).getId();
        taskId2 = taskService
                .saveWithSelfInjection(
                        new Task("Task_2", "Some task description of task #1",
                                LocalDate.parse("2019-07-18"), LocalDate.parse("2019-08-28"))).getId();
    }

    @After
    public void destroy() {
        taskService.deleteAll();
        userService.deleteAll();
        projectService.deleteAll();
    }

    @Test
    public void servicesExistsTest() {
        assertNotNull(userService);
        assertNotNull(projectService);
        assertNotNull(taskService);
    }

    @Test
    @DirtiesContext
    public void addNewTaskUnderUserAndProjectTest() {
        final String username = "user_001";
        final Task task = new Task("Task_3", "Some task description of task #3",
                LocalDate.parse("2019-07-18"), LocalDate.parse("2019-08-28"));
        final Task saved = taskService.saveWithSelfInjectionUnderUserAndProject(task, projectId, username);
        assertTrue(userService.findByUsernameWithEagerTasks(username).getTasks().contains(saved));
        assertTrue(projectService.findByIdWithEagerTasks(projectId).getTasks().contains(saved));
        assertEquals(taskService.findByIdWithEagerUser(saved.getId()).getUser().getUsername(), username);
        assertEquals(taskService.findByIdWithEagerProject(saved.getId()).getProject().getId(), projectId);
    }

    @Test
    @DirtiesContext
    public void deleteTaskUnderUserAndProjectTest() {
        final String username = "user_001";
        final Task task = new Task("Task_3", "Some task description of task #3",
                LocalDate.parse("2019-07-18"), LocalDate.parse("2019-08-28"));
        final Task saved = taskService.saveWithSelfInjectionUnderUserAndProject(task, projectId, username);
        taskService.deleteById(saved.getId());
        assertFalse(userService.findByUsernameWithEagerTasks(username).getTasks().contains(saved));
        assertFalse(projectService.findByIdWithEagerTasks(projectId).getTasks().contains(saved));
        assertEquals(2, taskService.findAll().size());
    }

    @Test
    @DirtiesContext
    public void deleteProjectWithCascadeTasksUnderUserTest() {
        final String username = "user_001";
        final Task task1 = taskService.findById(taskId1);
        final Task task2 = taskService.findById(taskId2);
        taskService.saveWithSelfInjectionUnderUserAndProject(task1, projectId, username);
        taskService.saveWithSelfInjectionUnderUserAndProject(task2, projectId, username);
        userService.addProjectAndUpdate(projectService.findById(projectId), username);

        projectService.deleteByIdUnderUser(projectId, username);

        assertFalse(userService.findByUsernameWithEagerTasks(username).getTasks().contains(task1));
        assertFalse(userService.findByUsernameWithEagerTasks(username).getTasks().contains(task2));
    }

    @Test
    @DirtiesContext
    public void assignTaskToUserTest() {
        final String username1 = "user_001";
        final String username2 = "user_002";

        taskService.assignToUser(taskId1, username1);

        final User user1 = userService.findByUsernameWithEagerTasks(username1);
        final User user2 = userService.findByUsernameWithEagerTasks(username2);
        final Task task = taskService.findByIdWithEagerUser(taskId1);
        final Set<Task> tasks1 = user1.getTasks();
        final Set<Task> tasks2 = user2.getTasks();

        assertEquals(1, tasks1.size());
        assertEquals(0, tasks2.size());
        assertTrue(tasks1.contains(task));
        assertFalse(tasks2.contains(task));
        assertEquals("user_001", task.getUser().getUsername());
    }

    @Test
    @DirtiesContext
    public void assignTaskToUserThenReassignItToAnotherUserTest() {
        final String username1 = "user_001";
        final String username2 = "user_002";

        taskService.assignToUser(taskId1, username1);
        taskService.assignToUser(taskId1, username2);

        final User user1AfterAssign = userService.findByUsernameWithEagerTasks(username1);
        final User user2AfterAssign = userService.findByUsernameWithEagerTasks(username2);
        final Task taskAfterAssign = taskService.findByIdWithEagerUser(taskId1);
        final Set<Task> tasks1AfterAssign = user1AfterAssign.getTasks();
        final Set<Task> tasks2AfterAssign = user2AfterAssign.getTasks();

        assertEquals(0, tasks1AfterAssign.size());
        assertEquals(1, tasks2AfterAssign.size());
        assertFalse(tasks1AfterAssign.contains(taskAfterAssign));
        assertTrue(tasks2AfterAssign.contains(taskAfterAssign));
        assertEquals("user_002", taskAfterAssign.getUser().getUsername());
    }

    @Test
    @DirtiesContext
    public void assignTaskToUserWileHeAlreadyHadItTest() {
        final String username = "user_002";
        exceptionRule.expect(TaskAlreadyExistsException.class);
        exceptionRule.expectMessage("User with username: '" + username + "' - Already has Task with ID: '" + taskId1 + "'!");

        taskService.assignToUser(taskId1, username);
        taskService.assignToUser(taskId1, username);
    }

    @Test
    @DirtiesContext
    public void whenRemoveTaskWhileItBelongToUserThenSuccessTest() {
        final String username = "user_001";

        taskService.assignToUser(taskId1, username);
        taskService.deleteById(taskId1);

        final Set<Task> tasks = userService.findByUsernameWithEagerTasks(username).getTasks();
        assertEquals(0, tasks.size());
    }
}
