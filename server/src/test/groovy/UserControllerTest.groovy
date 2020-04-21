import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser

import static data.TestData.getUser
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
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
        def result = performPost(REGISTER_USER_URL, user)

        then:
        result.andDo(print())
                .andExpect(status().isCreated())
    }

    @WithMockUser(username = "admin-user")
    def "Find user by username"() {
        given:
        saveUser("admin-user")

        when:
        def result = performGet(FIND_USER_BY_USERNAME_URL, "admin-user")

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

    def "When find user by username unauthorized then 403 error"() {
        given:
        saveUser("admin-user")

        when:
        def result = performGet(FIND_USER_BY_USERNAME_URL, randomAlphabetic(5))

        then:
        result.andDo(print())
                .andExpect(status().isForbidden())
    }

}
