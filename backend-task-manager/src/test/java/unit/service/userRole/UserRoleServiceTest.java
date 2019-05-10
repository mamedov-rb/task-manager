package unit.service.userRole;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.rmamedov.app.StartApp;
import ru.rmamedov.app.exception.RoleAlreadyExistsException;
import ru.rmamedov.app.exception.RoleNotFoundException;
import ru.rmamedov.app.exception.UserCantBeWithoutRoleException;
import ru.rmamedov.app.model.user.Role;
import ru.rmamedov.app.model.user.User;
import ru.rmamedov.app.service.interfaces.IRoleService;
import ru.rmamedov.app.service.interfaces.IUserService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Rustam Mamedov
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StartApp.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserRoleServiceTest {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void userServiceIsExistsTest() {
        assertNotNull(userService);
    }

    @Test
    public void roleServiceIsExistsTest() {
        assertNotNull(roleService);
    }

    @Test
    public void loadByUsernameWithEagerRolesTest() {
        final UserDetails userDetails = userService.loadUserByUsername("albertEnstain");
        assertFalse(userDetails.getAuthorities().isEmpty());
    }

    @Test
    @DirtiesContext
    public void registerNewUserWithEagerRolesWhileNoRolesTest() {
        userService.save(new User("tommyLi", "123qwe",
                "Tommy Li Djhons", 30, "8 900 800 70 70", "tommyLi@mail.ru"));
        final User user = userService.findByUsernameWithEagerRoles("tommyLi");
        assertNotNull(user);
        assertEquals(0, user.getRoles().size());
    }

    @Test
    @DirtiesContext
    public void registerNewUserWithEagerRolesTest() {
        final Role roleDeveloper = roleService.findByName("ROLE_DEVELOPER");
        final Role roleAdmin = roleService.findByName("ROLE_ADMIN");
        userService.save(new User("tommyLi", "123qwe",
                "Tommy Li Djhons", 30, "8 900 800 70 70", "tommyLi@mail.ru",
                new HashSet<>(Arrays.asList(roleAdmin, roleDeveloper))));
        final User user = userService.findByUsernameWithEagerRoles("tommyLi");
        assertNotNull(user);
        final Set<Role> roles = user.getRoles();
        assertEquals(2, roles.size());
        assertTrue(roles.containsAll(Arrays.asList(roleAdmin, roleDeveloper)));
    }

    @Test
    @DirtiesContext
    public void addRoleToUserTest() {
        final String username = "johnTravolta";
        final String roleName = "ROLE_PM";

        roleService.save(new Role(roleName));

        userService.addRoleAndUpdate(roleName, username);

        final Role role = roleService.findByNameWithEagerUsers(roleName);
        final User user = userService.findByUsernameWithEagerRoles(username);

        assertEquals(4, user.getRoles().size());
        assertTrue(user.getRoles().contains(role));
        assertTrue(role.getUsers().contains(user));
    }

    @Test
    @DirtiesContext
    public void addRoleToUserWileHeHasAlreadyHasItTest() {
        exceptionRule.expect(RoleAlreadyExistsException.class);
        exceptionRule.expectMessage("User with username: 'johnTravolta' - Already has Role with Name: 'ROLE_DEVELOPER'!");
        final String username = "johnTravolta";
        final String roleName = "ROLE_DEVELOPER";

        userService.addRoleAndUpdate(roleName, username);
    }

    @Test
    @DirtiesContext
    public void removeRoleFromUserTest() {
        final String username = "johnTravolta";
        final String roleName = "ROLE_ADMIN";

        userService.removeRoleAndUpdate(roleName, username);

        final User user = userService.findByUsernameWithEagerRoles(username);
        final Role role = roleService.findByNameWithEagerUsers(roleName);
        assertEquals(2, user.getRoles().size());
        assertFalse(user.getRoles().contains(role));
        assertFalse(role.getUsers().contains(user));
    }

    @Test
    @DirtiesContext
    public void removeLastRoleFromUserTest() {
        exceptionRule.expect(UserCantBeWithoutRoleException.class);
        exceptionRule.expectMessage("Can't remove last Role 'ROLE_DEVELOPER' from User - 'albertEnstain'!");
        final String username = "albertEnstain";
        final String roleName1 = "ROLE_TESTER";
        final String roleName2 = "ROLE_DEVELOPER";

        userService.removeRoleAndUpdate(roleName1, username);
        userService.removeRoleAndUpdate(roleName2, username);
    }

    @Test
    @DirtiesContext
    public void removeRoleFromUserWhenHiHasNoSuchRoleTest() {
        exceptionRule.expect(RoleNotFoundException.class);
        exceptionRule.expectMessage("User with username: 'albertEnstain' - Has no Role with Name: 'ROLE_ADMIN'!");
        final String username = "albertEnstain";
        final String roleName = "ROLE_ADMIN";
        userService.removeRoleAndUpdate(roleName, username);
    }
}
