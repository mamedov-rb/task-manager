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

    def cleanup() {
        clearDb()
    }

    @WithMockUser(username = "test-user")
    def "Create new project"() {
        given:
        saveUser("test-user")

        when:
        def result = performPost(CREATE_PROJECT, getProject())

        then:
        result.andDo(print())
                .andExpect(status().isCreated())
        userRepository.findAll().size() == 1
    }

    @WithMockUser(username = "test-user")
    def "Find project by id"() {
        given:
        saveProjectWithCreatedBy("test-user")
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

//    @WithMockUser(assignTo = "test-user") // TODO: fix
//    def "Find all by current user"() {
//        given:
//        saveProjectWithCreatedBy()
//
//        when:
//        def result = performGet(FIND_ALL_PROJECTS_BY_USER_ID, "test-user")
//
//        then:
//        result.andDo(print())
//                .andExpect(status().isOk())
//    }


    def "Create new project - 403"() {
        when:
        def result = performPost(CREATE_PROJECT, getProject())

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
