package controller

import helper.MockMvcHelper
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

    @WithMockUser
    def "Create new project"() {
        given:
        def user = userRepository.save(getUser())
        def project = getProject(user)

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
    def "Find project by id"() {

        given:
        def user = userRepository.save(getUser())
        def project = getProject(user)
        projectRepository.save(project)

        when:
        def result = performGet(FIND_PROJECT_BY_ID, project.id)

        then:
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath('$.id').value(project.id))
                .andExpect(jsonPath('$.name').isNotEmpty())
                .andExpect(jsonPath('$.description').isNotEmpty())
                .andExpect(jsonPath('$.startDate').isNotEmpty())
                .andExpect(jsonPath('$.endDate').isNotEmpty())
                .andExpect(jsonPath('$.created').isNotEmpty())

        cleanup:
        projectRepository.deleteAll()
        userRepository.deleteAll()
    }

}
