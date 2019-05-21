package ru.rmamedov.app.service;

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
import ru.rmamedov.app.exception.RoleAlreadyExistsException;
import ru.rmamedov.app.exception.RoleNotFoundException;
import ru.rmamedov.app.model.user.Role;
import ru.rmamedov.app.service.interfaces.IRoleService;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Rustam Mamedov
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StartApp.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class RoleServiceTest {

    @Autowired
    private IRoleService roleService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void init() {
        roleService.saveWithSelfInjection(new Role("ROLE_ADMIN"));
        roleService.saveWithSelfInjection(new Role("ROLE_DEVELOPER"));
        roleService.saveWithSelfInjection(new Role("ROLE_TESTER"));
    }

    @After
    public void destroy() {
        roleService.deleteAll();
    }

    @Test
    public void roleServiceIsExistsTest() {
        assertNotNull(roleService);
    }

    // find.
    @Test
    public void findRoleByNameTest() {
        final Role role = roleService.findByName("ROLE_ADMIN");
        assertNotNull(role);
    }
    @Test
    public void findNonexistentRoleByNameTest() {
        exceptionRule.expect(RoleNotFoundException.class);
        exceptionRule.expectMessage("Role with name: 'SOME_ROLE' - Not Found!");
        roleService.findByName("SOME_ROLE");
    }
    @Test
    public void findAllRolesTest() {
        final List<Role> roles = roleService.findAll();
        assertNotNull(roles);
        assertEquals(3, roles.size());
        assertTrue(roles.contains(roleService.findByName("ROLE_TESTER")));
        assertTrue(roles.contains(roleService.findByName("ROLE_DEVELOPER")));
        assertTrue(roles.contains(roleService.findByName("ROLE_ADMIN")));
    }

    @Test
    @DirtiesContext
    public void saveRoleTest() {
        final Role saved = roleService.saveWithSelfInjection(new Role("ROLE_PM"));
        assertNotNull(saved);
        assertEquals("ROLE_PM", saved.getName());
        assertEquals(4, roleService.findAll().size());
    }

    @Test
    @DirtiesContext
    public void saveRoleWithTheSameNameTest() {
        exceptionRule.expect(RoleAlreadyExistsException.class);
        exceptionRule.expectMessage("Role with name: 'ROLE_DEVELOPER' - Not saved!");
        roleService.saveWithSelfInjection(new Role("ROLE_DEVELOPER"));
    }

    @Test
    @DirtiesContext
    public void deleteRoleTest() {
        roleService.deleteByName("ROLE_TESTER");
        assertEquals(2, roleService.findAll().size());
    }
}