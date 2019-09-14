package controller

import helper.MockMvcHelper
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import ru.rmamedov.taskmanager.repository.ProjectRepository
import ru.rmamedov.taskmanager.repository.UserRepository

import static TestData.getProject
import static TestData.getUser
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * @author Rustam Mamedov
 */

class ProjectControllerTest extends MockMvcHelper {

    private final static String CREATE_PROJECT = "/api/project/create"

    private final static String FIND_PROJECT_BY_ID = "/api/project/find/{id}"

    @Autowired
    private UserRepository userRepository

    @Autowired
    private ProjectRepository projectRepository

    @WithMockUser(username = "test-user")
    def "Create new project"() {
        given:
        userRepository.save(getUser())
        def project = getProject()

        when:
        def result = performPost(CREATE_PROJECT, project)

        then:
        result.andDo(print())
                .andExpect(status().isCreated())
        userRepository.findAll().size() == 1

        cleanup:
        projectRepository.deleteAll()
        userRepository.deleteAll()
    }

    @WithMockUser
    def "Create new project if 'createdBy' Not found"() {
        given:
        def project = getProject()

        when:
        def result = performPost(CREATE_PROJECT, project)

        then:
        result.andDo(print())
                .andExpect(status().isBadRequest())

        cleanup:
        projectRepository.deleteAll()
    }

    @WithMockUser(username = "test-user")
    def "Find project by id"() {
        given:
        userRepository.save(getUser())
        def project = getProject()
        performPost(CREATE_PROJECT, project)
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

        cleanup:
        projectRepository.deleteAll()
        userRepository.deleteAll()
    }

    def "Create new project - 403"() {

        when:
        def result = performPost(CREATE_PROJECT, project)

        then:
        result.andDo(print())
                .andExpect(status().isForbidden())
    }

    def "Find project by id - 403"() {

        when:
        def result = performGet(FIND_PROJECT_BY_ID, Mockito.anyString())

        then:
        result.andDo(print())
                .andExpect(status().isForbidden())
    }

}
