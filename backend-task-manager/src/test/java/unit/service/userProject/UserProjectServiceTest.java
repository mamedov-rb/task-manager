package unit.service.userProject;

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
import ru.rmamedov.app.model.Project;
import ru.rmamedov.app.model.user.User;
import ru.rmamedov.app.service.interfaces.IProjectService;
import ru.rmamedov.app.service.interfaces.IUserService;

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

    @Test
    public void userServiceIsExistsTest() {
        assertNotNull(userService);
    }

    @Test
    public void projectServiceIsExistsTest() {
        assertNotNull(projectService);
    }

    @Test
    @DirtiesContext
    public void assignTheSameProjectToTwoUsersTest() {
        final String username1 = "albertEnstain";
        final String username2 = "johnTravolta";
        final String projectId = "projectID_1";

        userService.addProjectAndUpdate(projectService.findById(projectId), username1);

        final Project project = projectService.findByIdWithEagerUsers(projectId);
        final User user1 = userService.findByUsernameWithEagerProjects(username1);
        final User user2 = userService.findByUsernameWithEagerProjects(username2);
        final Set<Project> projects1 = user1.getProjects();
        final Set<Project> projects2 = user2.getProjects();
        final Set<User> users = project.getUsers();

        assertTrue(projects1.contains(project));
        assertFalse(projects2.contains(project));
        assertEquals(1, projects1.size());
        assertEquals(0, projects2.size());
        assertTrue(users.contains(user1));
        assertFalse(users.contains(user2));
        assertEquals(1, users.size());

        userService.addProjectAndUpdate(projectService.findById(projectId), username2);

        final Project projectAfterAssign = projectService.findByIdWithEagerUsers(projectId);
        final User user1AfterAssign = userService.findByUsernameWithEagerProjects(username1);
        final User user2AfterAssign = userService.findByUsernameWithEagerProjects(username2);
        final Set<Project> projects1AfterAssign = user1AfterAssign.getProjects();
        final Set<Project> projects2AfterAssign = user2AfterAssign.getProjects();
        final Set<User> usersAfterAssign = projectAfterAssign.getUsers();

        assertTrue(projects1AfterAssign.contains(projectAfterAssign));
        assertTrue(projects2AfterAssign.contains(projectAfterAssign));
        assertEquals(1, projects1AfterAssign.size());
        assertEquals(1, projects2AfterAssign.size());
        assertTrue(usersAfterAssign.contains(user1AfterAssign));
        assertTrue(usersAfterAssign.contains(user2AfterAssign));
        assertEquals(2, usersAfterAssign.size());
    }

    @Test
    @DirtiesContext
    public void assignProjectToUserWhileHiAlreadyHadItTest() {
        exceptionRule.expect(ProjectAlreadyExistsException.class);
        exceptionRule.expectMessage("User with username: 'albertEnstain' - Already has Project with ID: 'projectID_1'!");
        final String projectId = "projectID_1";
        final String username = "albertEnstain";

        userService.addProjectAndUpdate(projectService.findById(projectId), username);
        userService.addProjectAndUpdate(projectService.findById(projectId), username);
    }

    @Test
    @DirtiesContext
    public void dropUserFromProjectTest() {
        final String username1 = "albertEnstain";
        final String username2 = "johnTravolta";
        final String projectId = "projectID_1";

        userService.addProjectAndUpdate(projectService.findById(projectId), username1);
        userService.addProjectAndUpdate(projectService.findById(projectId), username2);

        userService.removeProjectAndUpdate(projectId, username1);

        assertEquals(1, projectService.findByIdWithEagerUsers(projectId).getUsers().size());
    }
}
