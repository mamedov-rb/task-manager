import org.apache.commons.lang3.RandomStringUtils
import ru.rmamedov.taskmanager.model.User

/**
 * @author Rustam Mamedov
 */

class UserTestData {

    static User getOneUser() {
        return User.builder()
                .firstName(RandomStringUtils.randomAlphabetic(10))
                .lastName(RandomStringUtils.randomAlphabetic(10))
                .username(RandomStringUtils.randomAlphabetic(10))
                .password(RandomStringUtils.randomAlphabetic(10))
                .email("user111@gmail.com")
                .phone("+7(800)100-10-10")
                .build()
    }
}
