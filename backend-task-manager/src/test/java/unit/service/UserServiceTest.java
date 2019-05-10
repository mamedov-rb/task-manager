package unit.service;

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
        final UserDetails user = userService.loadUserByUsername("johnTravolta");
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
        final User user = userService.findByUsernameWithEagerRoles("albertEnstain");
        assertNotNull(user);
        assertEquals("albertEnstain", user.getUsername());
        assertEquals("albertEnstain@mail.ru", user.getEmail());
        assertEquals(2, user.getRoles().size());
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
        assertTrue(users.contains(userService.findById("userID_2")));
    }

    @Test
    @DirtiesContext
    public void saveUserTest() {
        final User user = userService.save(new User("tommyLi", "123qwe",
                "Tommy Li Djhons", 30, "8 900 800 70 70", "tommyLi@mail.ru"));
        final User saved = userService.findById(user.getId());
        assertEquals(3, userService.findAll().size());
        assertNotNull(saved.getRegistered());
        assertNotNull(saved.getUpdated());
        assertEquals("tommyLi", saved.getUsername());
        assertTrue(encoder.matches("123qwe", saved.getPassword()));
    }
    @Test
    @DirtiesContext
    public void saveUserWithTheSameUsernameTest() {
        exceptionRule.expect(UserAlreadyExistsException.class);
        exceptionRule.expectMessage("User with username: 'albertEnstain' - Not saved!");
        userService.saveWithSelfInjection(new User("albertEnstain", "123qwe",
                "Tommy Li Djhons", 30, "+7 900 800 70 70", "tommyLi@mail.ru"));
    }

    @Test
    @DirtiesContext
    public void updateUserTest() {
        final User user = userService.findById("userID_2");
        user.setUsername("Qwe User");
        user.setEmail("qweuser@mail.ru");
        user.setPassword(encoder.encode("ASD123"));
        userService.update(user);
        final User updated = userService.findById("userID_2");
        assertNotNull(updated.getUpdated());
        assertEquals("Qwe User", updated.getUsername());
        assertEquals("qweuser@mail.ru", updated.getEmail());
        String password = updated.getPassword();
        assertTrue(encoder.matches("ASD123", password));
    }

    @Test
    @DirtiesContext
    public void fetchUserTest() {
        final User user = userService.findById("userID_2");
        user.setUsername("Qwe User");
        user.setEmail("qweuser@mail.ru");
        user.setPassword(encoder.encode("ASD123"));
        userService.patch(user);
        final User updated = userService.findById("userID_2");
        assertNotNull(updated.getUpdated());
        assertEquals("Qwe User", updated.getUsername());
        assertEquals("qweuser@mail.ru", updated.getEmail());
        final String password = updated.getPassword();
        assertTrue(encoder.matches("ASD123", password));
    }

    @Test
    @DirtiesContext
    public void deleteUserTest() {
        userService.deleteById("userID_2");
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
