package controller

import helper.MockMvcHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.context.support.WithMockUser
import ru.rmamedov.taskmanager.repository.ProjectRepository
import ru.rmamedov.taskmanager.repository.UserRepository

import static TestData.getProject
import static TestData.getUser
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author Rustam Mamedov
 */

class ProjectControllerTest extends MockMvcHelper {

    private final static String CREATE_PROJECT = "/api/project/create"

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
        def result = post(CREATE_PROJECT, project)

        then:
        result.andDo(print())
                .andExpect(status().isCreated())

        cleanup:
        projectRepository.deleteAll()
        userRepository.deleteAll()
    }

}
