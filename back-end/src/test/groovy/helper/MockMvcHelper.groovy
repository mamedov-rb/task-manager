package helper

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import ru.rmamedov.taskmanager.Application
import ru.rmamedov.taskmanager.repository.ProjectRepository
import ru.rmamedov.taskmanager.repository.TaskRepository
import ru.rmamedov.taskmanager.repository.UserRepository
import spock.lang.Specification

import static TestData.getProject
import static TestData.getUser
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

/**
 * @author Rustam Mamedov
 */

@SpringBootTest(classes = [Application.class])
@AutoConfigureMockMvc
class MockMvcHelper extends Specification {

    protected final static String CREATE_PROJECT = "/api/project/save"

    protected final static String FIND_PROJECT_BY_ID = "/api/project/find/{id}"

    protected final static String FIND_ALL_PROJECTS_BY_USER_ID = "/api/project/find-all-by/user/{username}"

    protected final static String ASSIGN_USER_TO_PROJECT = "/api/manager/assign/username/{username}/projectId/{id}"

    protected final static String LEAVE_PROJECT_UNDER_USER = "/api/manager/leave/username/{username}/projectId/{id}"

    protected final static String CREATE_AND_ASSIGN_TASK_TO_USER = "/api/manager/assign/task/to/user"

    protected final static String REASSIGN_TASK_TO_ANOTHER_USER = "/api/manager/reassign/task/{taskId}/user/{username}/by-project/{projectId}"

    protected final static String REGISTER_USER = "/api/user/save"

    protected final static String FIND_USER_BY_USERNAME = "/api/user/find/{assignTo}"

    protected final static String DELETE_TASK_BY_ID = "/api/task/delete/{id}"

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ObjectMapper mapper

    @Autowired
    protected UserRepository userRepository

    @Autowired
    protected ProjectRepository projectRepository

    @Autowired
    protected TaskRepository taskRepository

    protected performPost(String url, Object object) {
        mockMvc.perform(post(url)
                .content(mapper.writeValueAsString(object))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
    }

    protected performGet(String url, String... vars) {
        mockMvc.perform(get(url, vars))
    }

    protected performPatch(String url, String... vars) {
        mockMvc.perform(patch(url, vars))
    }

    protected performDelete(String url, String... vars) {
        mockMvc.perform(delete(url, vars))
    }

    def saveProjectWithCreatedBy(String createdBy) {
        saveUser(createdBy)
        def project = getProject()
        performPost(CREATE_PROJECT, project)
    }

    def saveUser(String username) {
        def user = getUser(username)
        performPost(REGISTER_USER, user)
    }

    def clearDb() { // TODO: leave projects.
        projectRepository.deleteAll()
        userRepository.deleteAll()
        taskRepository.deleteAll()
    }

}
