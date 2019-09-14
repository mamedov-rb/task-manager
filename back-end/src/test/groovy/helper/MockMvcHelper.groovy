package helper

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import ru.rmamedov.taskmanager.Application
import ru.rmamedov.taskmanager.repository.ProjectRepository
import ru.rmamedov.taskmanager.repository.UserRepository
import spock.lang.Specification

import static TestData.getProject
import static TestData.getUser
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

/**
 * @author Rustam Mamedov
 */

@SpringBootTest(classes = [Application.class])
@AutoConfigureMockMvc
class MockMvcHelper extends Specification {

    protected final static String CREATE_PROJECT = "/api/project/save"

    protected final static String FIND_PROJECT_BY_ID = "/api/project/find/{id}"

    protected final static String FIND_ALL_PROJECTS_BY_USER_ID = "/api/project/find/all/by/user/{username}"

    protected final static String ASSIGN_TO_USER = "/api/project/assign-to-user/{id}"

    protected final static String REGISTER_USER = "/api/user/save"

    protected final static String FIND_USER_BY_USERNAME = "/api/user/find/{username}"

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ObjectMapper mapper

    @Autowired
    protected UserRepository userRepository

    @Autowired
    protected ProjectRepository projectRepository

    protected performPost(String url, Object object) {
        mockMvc.perform(post(url)
                .content(mapper.writeValueAsString(object))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    protected performGet(String url, String...vars) {
        mockMvc.perform(get(url, vars))
    }

    protected performPatch(String url, String...vars) {
        mockMvc.perform(patch(url, vars))
    }

    def saveProjectWithCreatedBy() {
        performSavingUser()
        def project = getProject()
        performPost(CREATE_PROJECT, project)
    }

    def performSavingUser() {
        def user = getUser()
        performPost(REGISTER_USER, user)
    }

    def clear() {
//        projectRepository.deleteAll()
//        userRepository.deleteAll()
    }

}
