import org.hamcrest.Matchers
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import ru.rmamedov.taskmanager.model.Project

import static data.TestData.getProject
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * @author Rustam Mamedov
 */

class ProjectControllerTest extends MockMvcHelper {

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
                .andExpect(jsonPath('$.created').isNotEmpty())
                .andExpect(jsonPath('$.updated').isNotEmpty())
    }

    /**Finds all projects where current logged in user is member or creator*/
    @WithMockUser(username = "developer_01")
    def "Find all projects by current user"() {
        def developer_01 = "developer_01"
        given:
        saveProjectWithCreatedBy(developer_01)
        saveProjectWithCreatedBy(developer_01)
        for (Project p : projectRepository.findAll()) {
            performPatch(ASSIGN_USER_TO_PROJECT, developer_01, p.id)
        }

        when:
        def result = performGet(FIND_ALL_PROJECTS_BY_CURRENT_USER)

        then:
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect((jsonPath('$.[0].id')).isNotEmpty())
                .andExpect((jsonPath('$', Matchers.hasSize(2))))
    }

    def "Create new project - 401"() {
        when:
        def result = performPost(SAVE_PROJECT, getProject())

        then:
        result.andDo(print())
                .andExpect(status().isUnauthorized())
    }

    def "Find project by id - 401"() {
        when:
        def result = performGet(FIND_PROJECT_BY_ID, "123")

        then:
        result.andDo(print())
                .andExpect(status().isUnauthorized())
    }

}
