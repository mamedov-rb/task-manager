package controller

import helper.MockMvcHelper
import org.springframework.beans.factory.annotation.Autowired
import ru.rmamedov.taskmanager.repository.UserRepository

import static TestData.getUser
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author Rustam Mamedov
 */

class UserControllerTest extends MockMvcHelper {

    private final static String REGISTER_USER = "/api/user/create"

    @Autowired
    private UserRepository userRepository

    def "Register new user"() {
        given:
        def user = getUser()

        when:
        def result = performPost(REGISTER_USER, user)

        then:
        result.andDo(print())
                .andExpect(status().isCreated())

        cleanup:
        userRepository.deleteAll()
    }

}
