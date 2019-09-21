package controller

import helper.MockMvcHelper
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser

import static TestData.getProject
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * @author Rustam Mamedov
 */

class ProjectControllerTest extends MockMvcHelper {

    @WithMockUser(username = "admin-user")
    def "Save new project"() {
        given:
        saveUser("admin-user")

        when:
        def result = performPost(SAVE_PROJECT, getProject())

        then:
        result.andDo(print())
                .andExpect(status().isCreated())
        userRepository.findAll().size() == 1
    }

    @WithMockUser(username = "admin-user")
    def "Find project by id"() {
        given:
        saveProjectWithCreatedBy("admin-user")
        def id = projectRepository.findAll().stream().findFirst().get().id

        when:
        def result = performGet(FIND_PROJECT_BY_ID, id)

        then:
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$.id').value(id))
                .andExpect(jsonPath('$.name').isNotEmpty())
                .andExpect(jsonPath('$.description').isNotEmpty())
                .andExpect(jsonPath('$.startDate').isNotEmpty())
                .andExpect(jsonPath('$.endDate').isNotEmpty())
                .andExpect(jsonPath('$.created').isNotEmpty())
    }

    @WithMockUser(username = "admin-user")
    def "Find all projects by current user"() {
        def developer = "admin-user"
        given:
        saveProjectWithCreatedBy(developer)
        def id = projectRepository.findAll().stream().findFirst().get().id
        performPatch(ASSIGN_USER_TO_PROJECT, developer, id)

        when:
        def result = performGet(FIND_ALL_PROJECTS_BY_USERNAME, developer)

        then:
        result.andDo(print())
                .andExpect(status().isOk())

        userRepository.findUserWithEagerProjects(developer).get().projects.size() == 1
    }

    def "Create new project - 403"() {
        when:
        def result = performPost(SAVE_PROJECT, getProject())

        then:
        result.andDo(print())
                .andExpect(status().isForbidden())
    }

    def "Find project by id - 403"() {
        when:
        def result = performGet(FIND_PROJECT_BY_ID, "123")

        then:
        result.andDo(print())
                .andExpect(status().isForbidden())
    }

}
