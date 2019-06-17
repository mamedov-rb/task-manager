package ru.rmamedov.app.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.rmamedov.app.StartApp;
import ru.rmamedov.app.exception.UserAlreadyExistsException;
import ru.rmamedov.app.exception.UserNotFoundException;
import ru.rmamedov.app.model.user.User;
import ru.rmamedov.app.service.interfaces.IUserService;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Rustam Mamedov
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StartApp.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceTest {

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private String userId = null;

    @Before
    public void init() {
        userService
                .saveWithSelfInjection(new User("user_001", "123qwe", "John Travolta", 40,
                        "+7(900)800-70-07", "johnTravolta@mail.ru"));
        userId = userService
                .saveWithSelfInjection(new User("user_002", "123qwe", "Albert Enstain", 30,
                         "+7(900)800-70-07", "albertEnstain@mail.ru")).getId();
    }

    @After
    public void destroy() {
        userService.deleteAll();
    }


    @Test
    public void userServiceIsExistsTest() {
        assertNotNull(userService);
    }

    @Test
    public void encoderIsExistsTest() {
        assertNotNull(encoder);
    }

    // Find.
    @Test
    public void loadUserDetailsByUsernameTest() {
        final UserDetails user = userService.loadUserByUsername("user_001");
        assertNotNull(user);
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isEnabled());
    }
    @Test
    public void loadNonexistentUserDetailsByUsernameTest() {
        exceptionRule.expect(UsernameNotFoundException.class);
        exceptionRule.expectMessage("Username: 'Travolta' - Not Found!");
        userService.loadUserByUsername("Travolta");
    }
    @Test
    public void findUserByUsernameTest() {
        final User user = userService.findByUsernameWithEagerRoles("user_002");
        assertNotNull(user);
        assertEquals("user_002", user.getUsername());
        assertEquals("albertEnstain@mail.ru", user.getEmail());
    }
    @Test
    public void findNonexistentUserByIdTest() {
        exceptionRule.expect(UserNotFoundException.class);
        exceptionRule.expectMessage("User with ID: 'ID' - Not Found!");
        userService.findById("ID");
    }
    @Test
    public void findAllUsersTest() {
        final List<User> users = userService.findAll();
        assertNotNull(users);
        assertEquals(2, users.size());
        assertTrue(users.contains(userService.findById(userId)));
    }

    // save.
    @Test
    @DirtiesContext
    public void saveUserTest() {
        final User user = userService.saveWithSelfInjection(new User("tommyLi", "123qwe",
                "Tommy Li Djhons", 30, "8 900 800 70 70", "tommyLi@mail.ru"));
        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals(3, userService.findAll().size());
        assertNotNull(user.getRegistered());
        assertNotNull(user.getUpdated());
        assertEquals("tommyLi", user.getUsername());
        assertTrue(encoder.matches("123qwe", user.getPassword()));
    }
    @Test
    @DirtiesContext
    public void saveUserWithTheSameUsernameTest() {
        exceptionRule.expect(UserAlreadyExistsException.class);
        exceptionRule.expectMessage("User with username: 'user_002' - Not saved!");
        userService.saveWithSelfInjection(new User("user_002", "123qwe",
                "Tommy Li Djhons", 30, "+7 900 800 70 70", "tommyLi@mail.ru"));
    }

    // update.
    @Test
    @DirtiesContext
    public void updateUserTest() {
        final User user = userService.findById(userId);
        user.setUsername("Qwe User");
        user.setEmail("qweuser@mail.ru");
        user.setPassword(encoder.encode("ASD123"));
        final User updated = userService.update(user);
        assertNotNull(updated.getUpdated());
        assertEquals("Qwe User", updated.getUsername());
        assertEquals("qweuser@mail.ru", updated.getEmail());
        String password = updated.getPassword();
        assertTrue(encoder.matches("ASD123", password));
    }
    @Test
    @DirtiesContext
    public void fetchUserTest() {
        final User user = userService.findById(userId);
        user.setUsername("Qwe User");
        user.setEmail("qweuser@mail.ru");
        user.setPassword(encoder.encode("ASD123"));
        userService.patch(user);
        final User updated = userService.findById(userId);
        assertNotNull(updated.getUpdated());
        assertEquals("Qwe User", updated.getUsername());
        assertEquals("qweuser@mail.ru", updated.getEmail());
        final String password = updated.getPassword();
        assertTrue(encoder.matches("ASD123", password));
    }

    // delete.
    @Test
    @DirtiesContext
    public void deleteUserTest() {
        userService.deleteById(userId);
        assertEquals(1, userService.findAll().size());
    }
    @Test
    @DirtiesContext
    public void deleteAllUsersTest() {
        exceptionRule.expect(UserNotFoundException.class);
        exceptionRule.expectMessage("There are is no any users!");
        userService.deleteAll();
        userService.findAll();
    }
}
