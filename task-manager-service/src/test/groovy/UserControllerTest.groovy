import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser

import static data.TestData.getUser
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * @author Rustam Mamedov
 */

class UserControllerTest extends MockMvcHelper {

    def "Save new user"() {
        given:
        def user = getUser("admin-user")

        when:
        def result = performPost(REGISTER_USER, user)

        then:
        result.andDo(print())
                .andExpect(status().isCreated())
    }

    @WithMockUser(username = "admin-user")
    def "Find user by username"() {
        given:
        saveUser("admin-user")

        when:
        def result = performGet(FIND_USER_BY_USERNAME, "admin-user")

        then:
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$.id').isNotEmpty())
                .andExpect(jsonPath('$.firstName').isNotEmpty())
                .andExpect(jsonPath('$.lastName').isNotEmpty())
                .andExpect(jsonPath('$.phone').value("+7(800)100-10-10"))
                .andExpect(jsonPath('$.email').value("user@gmail.com"))
    }

    @WithMockUser(username = "admin-user") // fix
    def "Find all users of project"() {

    }

    def "Find user by username - 401"() {
        given:
        saveUser("admin-user")

        when:
        def result = performGet(FIND_USER_BY_USERNAME, "admin-user")

        then:
        result.andDo(print())
                .andExpect(status().isUnauthorized())
    }

}
