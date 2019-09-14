package controller

import helper.MockMvcHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import ru.rmamedov.taskmanager.repository.UserRepository

import static TestData.getUser
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * @author Rustam Mamedov
 */

class UserControllerTest extends MockMvcHelper {

    private final static String REGISTER_USER = "/api/user/create"

    private final static String FIND_USER_BY_USERNAME = "/api/user/find/{username}"

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

    @WithMockUser
    def "Find user by username"() {
        given:
        def user = getUser()
        performPost(REGISTER_USER, user)

        when:
        def result = performGet(FIND_USER_BY_USERNAME, user.username)

        then:
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$.id').isNotEmpty())
                .andExpect(jsonPath('$.firstName').isNotEmpty())
                .andExpect(jsonPath('$.lastName').isNotEmpty())
                .andExpect(jsonPath('$.username').value(user.username))
                .andExpect(jsonPath('$.phone').value("+7(800)100-10-10"))
                .andExpect(jsonPath('$.email').value("user@gmail.com"))

        cleanup:
        userRepository.deleteAll()
    }

    def "Find user by username - 403"() {
        given:
        def user = getUser()
        performPost(REGISTER_USER, user)

        when:
        def result = performGet(FIND_USER_BY_USERNAME, user.username)

        then:
        result.andDo(print())
                .andExpect(status().isForbidden())

        cleanup:
        userRepository.deleteAll()
    }

}
