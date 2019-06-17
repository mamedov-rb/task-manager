package ru.rmamedov.app.service.userRole;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
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

import java.util.*;

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

    @Before
    public void init() {
        roleService.saveWithSelfInjection(new Role("ROLE_ADMIN"));
        roleService.saveWithSelfInjection(new Role("ROLE_DEVELOPER"));
        roleService.saveWithSelfInjection(new Role("ROLE_TESTER"));

        userService
                .saveWithSelfInjection(new User("user_001", "123qwe", "John Travolta", 40,
                        "+7(900)800-70-07", "johnTravolta@mail.ru"));
        userService
                .saveWithSelfInjection(new User("user_002", "123qwe", "Albert Enstain", 30,
                        "+7(900)800-70-07", "albertEnstain@mail.ru"));
    }

    @After
    public void destroy() {
        userService.deleteAll();
        roleService.deleteAll();
    }

    @Test
    public void servicesExistsTest() {
        assertNotNull(userService);
        assertNotNull(roleService);
    }

    // find.
    @Test
    @DirtiesContext
    public void loadByUsernameWithEagerRolesTest() {
        final String username = "user_002";
        final String adminRoleName = "ROLE_ADMIN";
        final String developerRoleName = "ROLE_DEVELOPER";
        userService.addRoleAndUpdate(adminRoleName, username);
        userService.addRoleAndUpdate(developerRoleName, username);

        final UserDetails userDetails = userService.loadUserByUsername(username);
        final Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        assertFalse(authorities.isEmpty());
        assertEquals(2, authorities.size());
    }
    @Test
    @DirtiesContext
    public void findRoleByNameWithEagerUsersTest() {
        final String developerRoleName = "ROLE_DEVELOPER";
        userService.addRoleAndUpdate(developerRoleName, "user_001");
        userService.addRoleAndUpdate(developerRoleName, "user_002");

        final Role role = roleService.findByNameWithEagerUsers("ROLE_DEVELOPER");
        assertNotNull(role);
        assertEquals(2, role.getUsers().size());
    }

    // save.
    @Test
    @DirtiesContext
    public void registerNewUserWithNoRolesTest() {
        userService.saveWithSelfInjection(new User("user_003", "123qwe",
                "Tommy Li Djhons", 30, "8 900 800 70 70", "tommyLi@mail.ru"));
        final User user = userService.findByUsernameWithEagerRoles("user_003");
        assertNotNull(user);
        assertEquals(0, user.getRoles().size());
    }
    @Test
    @DirtiesContext
    public void registerNewUserWithOneRolesTest() {
        final Role roleDeveloper = roleService.findByName("ROLE_DEVELOPER");
        userService.saveWithSelfInjection(new User("user_003", "123qwe",
                "Tommy Li Djhons", 30, "8 900 800 70 70", "tommyLi@mail.ru",
                new HashSet<>(Arrays.asList(roleDeveloper))));
        final User user = userService.findByUsernameWithEagerRoles("user_003");
        final Set<Role> roles = user.getRoles();
        assertNotNull(user);
        assertEquals(1, roles.size());
        assertTrue(roles.contains(roleDeveloper));
    }
    @Test
    @DirtiesContext
    public void addRoleToUserTest() {
        final String username = "user_001";
        final String pmRoleName = "ROLE_PM";
        final String adminRoleName = "ROLE_ADMIN";
        final String testerRoleName = "ROLE_TESTER";
        final String developerRoleName = "ROLE_DEVELOPER";
        userService.addRoleAndUpdate(adminRoleName, username);
        userService.addRoleAndUpdate(developerRoleName, username);
        userService.addRoleAndUpdate(testerRoleName, username);

        roleService.saveWithSelfInjection(new Role(pmRoleName));
        userService.addRoleAndUpdate(pmRoleName, username);

        final Role role = roleService.findByNameWithEagerUsers(pmRoleName);
        final User user = userService.findByUsernameWithEagerRoles(username);

        assertEquals(4, user.getRoles().size());
        assertTrue(user.getRoles().contains(role));
        assertTrue(role.getUsers().contains(user));
    }
    @Test
    @DirtiesContext
    public void addRoleToUserWileHeHasAlreadyHaveItTest() {
        final String username = "user_001";
        final String roleName = "ROLE_DEVELOPER";
        userService.addRoleAndUpdate(roleName, username);

        exceptionRule.expect(RoleAlreadyExistsException.class);
        exceptionRule.expectMessage("User with username: '" + username + "' - Already has Role with Name: '" + roleName + "'!");

        userService.addRoleAndUpdate(roleName, username);
    }

    // delete.
    @Test
    @DirtiesContext
    public void removeRoleFromUserTest() {
        final String username = "user_002";
        final String adminRoleName = "ROLE_ADMIN";
        final String testerRoleName = "ROLE_TESTER";

        userService.addRoleAndUpdate(adminRoleName, username);
        userService.addRoleAndUpdate(testerRoleName, username);
        userService.removeRoleAndUpdate(testerRoleName, username);

        final User user = userService.findByUsernameWithEagerRoles(username);
        final Set<Role> roles = user.getRoles();
        final Role role = roleService.findByNameWithEagerUsers(testerRoleName);
        final Set<User> users = role.getUsers();

        assertEquals(1, user.getRoles().size());
        assertFalse(roles.contains(role));
        assertFalse(users.contains(user));
    }
    @Test
    @DirtiesContext
    public void removeLastRoleFromUserTest() {
        final String username = "user_002";
        final String developerRoleName = "ROLE_DEVELOPER";
        userService.addRoleAndUpdate(developerRoleName, username);

        exceptionRule.expect(UserCantBeWithoutRoleException.class);
        exceptionRule.expectMessage("Can't remove last Role '" + developerRoleName + "' from User - '" + username + "'!");

        userService.removeRoleAndUpdate(developerRoleName, username);
    }
    @Test
    @DirtiesContext
    public void removeRoleFromUserWhenHiHasNoSuchRoleTest() {
        final String username = "user_002";
        final String developerRoleName = "ROLE_DEVELOPER";

        exceptionRule.expect(RoleNotFoundException.class);
        exceptionRule.expectMessage("User with username: '" + username + "' - Has no Role with Name: '" + developerRoleName + "'!");

        userService.removeRoleAndUpdate(developerRoleName, username);
    }
}
