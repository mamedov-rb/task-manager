package ru.rmamedov.app.service.userProject;

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
import ru.rmamedov.app.exception.ProjectAlreadyExistsException;
import ru.rmamedov.app.exception.ProjectNotFoundException;
import ru.rmamedov.app.model.Project;
import ru.rmamedov.app.model.user.User;
import ru.rmamedov.app.service.interfaces.IProjectService;
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
public class UserProjectServiceTest {

    @Autowired
    private IUserService userService;

    @Autowired
    private IProjectService projectService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private String projectId = null;

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
    }

    @After
    public void destroy() {
        userService.deleteAll();
        projectService.deleteAll();
    }

    @Test
    public void servicesExistsTest() {
        assertNotNull(userService);
        assertNotNull(projectService);
    }

    @Test
    @DirtiesContext
    public void assignProjectToUserTest() {
        final String username1 = "user_001";

        final Project foundProject = projectService.findById(projectId);
        userService.addProjectAndUpdate(foundProject, username1);

        final Project project = projectService.findByIdWithEagerUsers(projectId);
        final User user1 = userService.findByUsernameWithEagerProjects(username1);

        final Set<Project> projects1 = user1.getProjects();
        final Set<User> users = project.getUsers();

        assertTrue(projects1.contains(project));
        assertEquals(1, projects1.size());
        assertTrue(users.contains(user1));
        assertEquals(1, users.size());
    }

    @Test
    @DirtiesContext
    public void assignTheSameProjectToTwoUsersTest() {
        final String username1 = "user_001";
        final String username2 = "user_002";

        final Project foundProject = projectService.findById(projectId);
        userService.addProjectAndUpdate(foundProject, username1);
        userService.addProjectAndUpdate(foundProject, username2);

        final Project project = projectService.findByIdWithEagerUsers(projectId);
        final User user1 = userService.findByUsernameWithEagerProjects(username1);
        final User user2 = userService.findByUsernameWithEagerProjects(username2);

        final Set<Project> projects1 = user1.getProjects();
        final Set<Project> projects2 = user2.getProjects();
        final Set<User> users = project.getUsers();

        assertTrue(projects1.contains(project));
        assertTrue(projects2.contains(project));
        assertEquals(1, projects1.size());
        assertEquals(1, projects2.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
        assertEquals(2, users.size());
    }

    @Test
    @DirtiesContext
    public void assignProjectToUserWhileHiAlreadyHadItTest() {
        exceptionRule.expect(ProjectAlreadyExistsException.class);
        exceptionRule.expectMessage("User with username: 'user_002' - Already has Project with ID: '" + projectId + "'!");
        final String username = "user_002";
        final Project project = projectService.findById(projectId);
        userService.addProjectAndUpdate(project, username);
        userService.addProjectAndUpdate(project, username);
    }

    @Test
    @DirtiesContext
    public void dropUserFromProjectTest() {
        final String username1 = "user_001";
        final Project project = projectService.findById(projectId);
        userService.addProjectAndUpdate(project, username1);
        assertEquals(1, projectService.findByIdWithEagerUsers(projectId).getUsers().size());
        assertEquals(1, userService.findByUsernameWithEagerProjects(username1).getProjects().size());

        userService.removeProjectAndUpdate(projectId, username1);
        assertEquals(0, projectService.findByIdWithEagerUsers(projectId).getUsers().size());
        assertEquals(0, userService.findByUsernameWithEagerProjects(username1).getProjects().size());
    }

    @Test
    @DirtiesContext
    public void deleteProjectUnderUserTest() {
        final String username1 = "user_001";
        final Project project = projectService.findById(projectId);

        userService.addProjectAndUpdate(project, username1);

        assertEquals(1, projectService.findByIdWithEagerUsers(projectId).getUsers().size());
        assertEquals(1, userService.findByUsernameWithEagerProjects(username1).getProjects().size());
        assertEquals(1, projectService.findAll().size());

        projectService.deleteByIdUnderUser(projectId, username1);
        assertFalse(userService.findByUsernameWithEagerProjects(username1).getProjects().contains(project));
    }

    @Test
    @DirtiesContext
    public void deleteProjectUnderUserWhenUserHasNoSuchProjectTest() {
        final String username = "user_001";
        exceptionRule.expect(ProjectNotFoundException.class);
        exceptionRule.expectMessage("User with username: '" + username + "' - Has no Project with ID: '" + projectId + "'!");
        projectService.deleteByIdUnderUser(projectId, username);
    }
}
