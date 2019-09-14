package controller

import helper.MockMvcHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import ru.rmamedov.taskmanager.repository.UserRepository

import static UserTestData.getOneUser
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
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
        def user = getOneUser()

        when:
        def result = post(REGISTER_USER, user)

        then:
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$.id').isNotEmpty())
                .andExpect(jsonPath('$.firstName').isNotEmpty())
                .andExpect(jsonPath('$.lastName').isNotEmpty())
                .andExpect(jsonPath('$.phone').value("+7(800)100-10-10"))
                .andExpect(jsonPath('$.email').value("user111@gmail.com"))

        cleanup:
        userRepository.deleteAll()
    }

}
