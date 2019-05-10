package unit.service;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Test
    public void roleServiceIsExistsTest() {
        assertNotNull(roleService);
    }

    @Test
    public void findRoleByNameTest() {
        final Role role = roleService.findByName("ROLE_ADMIN");
        assertNotNull(role);
    }

    @Test
    public void findRoleByNameWithEagerUsersTest() {
        final Role role = roleService.findByNameWithEagerUsers("ROLE_DEVELOPER");
        assertNotNull(role);
        assertEquals(2, role.getUsers().size());
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
    }

    @Test
    @DirtiesContext
    public void saveUniqueRoleTest() {
        final Role saved = roleService.save(new Role("ROLE_PM"));
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
    public void deleteUserTest() {
        exceptionRule.expect(DataIntegrityViolationException.class);
        roleService.deleteByName("ROLE_TESTER");
        assertEquals(2, roleService.findAll().size());
    }
}
