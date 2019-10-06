import com.fasterxml.jackson.databind.ObjectMapper
import groovy.sql.Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import ru.rmamedov.taskmanager.Application
import ru.rmamedov.taskmanager.repository.CommentRepository
import ru.rmamedov.taskmanager.repository.ProjectRepository
import ru.rmamedov.taskmanager.repository.TaskRepository
import ru.rmamedov.taskmanager.repository.UserRepository
import ru.rmamedov.taskmanager.service.ProjectManagerService
import spock.lang.Shared
import spock.lang.Specification

import javax.sql.DataSource

import static data.TestData.getProject
import static data.TestData.getUser
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

/**
 * @author Rustam Mamedov
 */

@ActiveProfiles("test")
@SpringBootTest(
        classes = [Application.class],
        webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
class MockMvcHelper extends Specification {

    protected final static String SAVE_PROJECT = "/api/manager/project/save"

    protected final static String FIND_PROJECT_BY_ID = "/api/project/find/{id}"

    protected final static String IS_ASSIGNED_TO_PROJECT = "/api/project/contains-user"

    protected final static String FIND_ALL_PROJECTS_BY_CURRENT_USER = "/api/project/all"

    protected final static String ASSIGN_USER_TO_PROJECT = "/api/manager/assign/username/{username}/projectId/{id}"

    protected final static String LEAVE_PROJECT_UNDER_USER = "/api/manager/leave/username/{username}/projectId/{id}"

    protected final static String LEAVE_ALL_PROJECTS = "/api/manager/leave/all/projects/user/{username}"

    protected final static String SAVE_AND_ASSIGN_TASK_TO_USER = "/api/manager/assign/task/to/user"

    protected final static String REASSIGN_TASK_TO_ANOTHER_USER = "/api/manager/reassign/task/{taskId}/user/{username}/by-project/{projectId}"

    protected final static String REGISTER_USER = "/api/user/save"

    protected final static String FIND_USER_BY_USERNAME = "/api/user/find/{assignTo}"

    protected final static String FIND_ALL_USERS_OF_PROJECT = "/api/manager/users/{projectId}"

    protected final static String DELETE_TASK_BY_ID = "/api/task/delete/{id}"

    protected final static String DELETE_PROJECT_BY_ID = "/api/manager/delete/project/id/{id}/user/{username}"

    protected final static String SAVE_COMMENT_UNDER_USER_TASK = "/api/manager/comment/save"

    protected final static String DELETE_COMMENT_UNDER_USER_TASK = "/api/manager/comment/delete/{id}"

    protected final static String FIND_ALL_COMMENTS_OF_TASK = "/api/comment/all/taskId/{taskId}"

    protected final static String DELETE_USER_UNDER_PROJECTS_TASKS = "/api/manager/user/delete/{username}"

    protected final static String FIND_ALL_TASKS_BY_ASSIGNED_TO_AND_PROJECT = "/api/task/all/by/assignedTo/projectId/{projectId}"

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

    @Autowired
    protected CommentRepository commentRepository

    @Autowired
    protected ProjectManagerService projectManagerService

    @Shared
    Sql sql

    @Autowired
    DataSource dataSource

    def cleanup() {
        sql = new Sql(dataSource)
        sql.execute("DELETE FROM comment")
        sql.execute("DELETE FROM task")
        sql.execute("DELETE FROM users_projects")
        sql.execute("DELETE FROM project")
        sql.execute("DELETE FROM users_roles")
        sql.execute("DELETE FROM role")
        sql.execute("DELETE FROM app_user")
    }

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
        performPost(SAVE_PROJECT, project)
    }


    def saveUser(String username) {
        def user = getUser(username)
        performPost(REGISTER_USER, user)
    }

}
