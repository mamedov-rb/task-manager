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

    def "Create new user"() {
        given:
        def user = getUser("test-user")

        when:
        def result = performPost(REGISTER_USER, user)

        then:
        result.andDo(print())
                .andExpect(status().isCreated())
    }

    @WithMockUser
    def "Find user by username"() {
        given:
        performSavingUser("test-user")

        when:
        def result = performGet(FIND_USER_BY_USERNAME, "test-user")

        then:
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$.id').isNotEmpty())
                .andExpect(jsonPath('$.firstName').isNotEmpty())
                .andExpect(jsonPath('$.lastName').isNotEmpty())
                .andExpect(jsonPath('$.username').value("test-user"))
                .andExpect(jsonPath('$.phone').value("+7(800)100-10-10"))
                .andExpect(jsonPath('$.email').value("user@gmail.com"))
    }

    @WithMockUser(username = "test-user")
    def "Assign project to user"() {
        given:
        saveProjectWithCreatedBy("test-user")
        def developerUsername = "developer-user"
        performSavingUser(developerUsername)

        def id = projectRepository.findAll().stream().findFirst().get().id

        when:
        def result = performPatch(ASSIGN_TO_PROJECT, developerUsername, id)

        then:
        result.andDo(print())
                .andExpect(status().isCreated())
        userRepository.findUserWithEagerProjects(developerUsername).get().projects.size() == 1
        projectRepository.findByIdWithEagerUsers(id).get().users.size() == 1

        // TODO: leave project before cleanup()
    }

    def "Find user by username - 403"() {
        given:
        performSavingUser("test-user")

        when:
        def result = performGet(FIND_USER_BY_USERNAME, "test-user")

        then:
        result.andDo(print())
                .andExpect(status().isForbidden())
    }

}
