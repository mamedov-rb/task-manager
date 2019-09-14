package controller

import helper.MockMvcHelper
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser

import static TestData.getUser
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * @author Rustam Mamedov
 */

class UserControllerTest extends MockMvcHelper {

    def cleanup() {
        clear()
    }

    def "Register new user"() {
        given:
        def user = getUser()

        when:
        def result = performPost(REGISTER_USER, user)

        then:
        result.andDo(print())
                .andExpect(status().isCreated())
    }

    @WithMockUser
    def "Find user by username"() {
        given:
        performSavingUser()

        when:
        def result = performGet(FIND_USER_BY_USERNAME, getUser().username)

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
    }

    def "Find user by username - 403"() {
        given:
        performSavingUser()

        when:
        def result = performGet(FIND_USER_BY_USERNAME, getUser().username)

        then:
        result.andDo(print())
                .andExpect(status().isForbidden())
    }

}
