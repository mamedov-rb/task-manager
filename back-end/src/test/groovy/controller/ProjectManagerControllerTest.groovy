package controller

import helper.MockMvcHelper
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.ResultActions
import ru.rmamedov.taskmanager.model.Project

import static TestData.getCreateTaskRequest
import static TestData.getSaveCommentRequest
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author Rustam Mamedov
 */

class ProjectManagerControllerTest extends MockMvcHelper {

    @WithMockUser(username = "admin-user")
    def "Assign user to project"() {
        given:
        def developer = "developer-user"
        saveProjectWithCreatedBy("admin-user")
        saveUser(developer)
        def id = projectRepository.findAll().stream().findFirst().get().id

        when:
        def result = performPatch(ASSIGN_USER_TO_PROJECT, developer, id)

        then:
        result.andDo(print())
                .andExpect(status().isCreated())

        userRepository.findUserWithEagerProjects(developer).get().projects.size() == 1
        projectRepository.findByIdWithEagerUsers(id).get().users.size() == 1
    }

    @WithMockUser(username = "admin-user")
    def "Assign project to multiple users"() {
        given:
        saveProjectWithCreatedBy("admin-user")
        def developer_01 = "developer_01"
        def developer_02 = "developer_02"
        def developer_03 = "developer_03"
        saveUser(developer_01)
        saveUser(developer_02)
        saveUser(developer_03)
        def id = projectRepository.findAll().stream().findFirst().get().id
        performPatch(ASSIGN_USER_TO_PROJECT, developer_01, id)
        performPatch(ASSIGN_USER_TO_PROJECT, developer_02, id)
        performPatch(ASSIGN_USER_TO_PROJECT, developer_03, id)

        when:
        def project = projectRepository.findByIdWithEagerUsers(id).get()

        then:
        project.users.size() == 3
        userRepository.findUserWithEagerProjects(developer_01).get().projects.size() == 1
        userRepository.findUserWithEagerProjects(developer_02).get().projects.size() == 1
        userRepository.findUserWithEagerProjects(developer_03).get().projects.size() == 1
    }

    @WithMockUser(username = "admin-user")
    def "Leave project with tasks under user"() {
        given:
        def developer_01 = "developer_01"
        saveProjectWithCreatedBy("admin-user")
        saveUser(developer_01)
        def projectId = projectRepository.findAll().stream().findFirst().get().id
        performPatch(ASSIGN_USER_TO_PROJECT, developer_01, projectId)
        performPost(SAVE_AND_ASSIGN_TASK_TO_USER, getCreateTaskRequest(projectId, developer_01))

        when:
        def result = performPatch(LEAVE_PROJECT_UNDER_USER, developer_01, projectId)

        then:
        result.andDo(print())
                .andExpect(status().isCreated())

        userRepository.findUserWithEagerProjects(developer_01).get().projects.size() == 0
        projectRepository.findByIdWithEagerUsers(projectId).get().users.size() == 0
        taskRepository.findAll().size() == 0
    }

    @WithMockUser(username = "admin-user")
    def "Leave all projects with tasks under user"() {
        given:
        def developer_01 = "developer_01"
        saveUser(developer_01)
        saveProjectWithCreatedBy("admin-user")
        saveProjectWithCreatedBy("admin-user")
        saveProjectWithCreatedBy("admin-user")
        for(Project p : projectRepository.findAll()) {
            performPatch(ASSIGN_USER_TO_PROJECT, developer_01, p.id)
            performPost(SAVE_AND_ASSIGN_TASK_TO_USER, getCreateTaskRequest(p.id, developer_01))
        }

        when:
        def result = performPatch(LEAVE_ALL_PROJECTS, developer_01)

        then:
        result.andDo(print())
                .andExpect(status().isCreated())

        userRepository.findUserWithEagerProjects(developer_01).get().projects.size() == 0
        taskRepository.findAll().size() == 0
    }

    @WithMockUser(username = "admin-user")
    def "Create and assign task to user and project"() {
        given:
        def developer = "developer-user"
        saveProjectWithCreatedBy("admin-user")
        saveUser(developer)
        def projectId = projectRepository.findAll().stream().findFirst().get().id
        performPatch(ASSIGN_USER_TO_PROJECT, developer, projectId)

        when:
        def result = performPost(SAVE_AND_ASSIGN_TASK_TO_USER, getCreateTaskRequest(projectId, developer))

        then:
        result.andDo(print())
                .andExpect(status().isCreated())

        def id = taskRepository.findAll().stream().findFirst().get().id
        def task = taskRepository.findByIdWithEagerCreatedByAndAssignedTo(id).get()
        task != null
        task.project.id == projectId
        task.createdBy.username == "admin-user"
        task.assignedTo.username == developer
    }

    @WithMockUser(username = "admin-user")
    def "Reassign task to another user"() {
        given:
        saveProjectWithCreatedBy("admin-user")

        def developer_01 = "developer_01"
        saveUser(developer_01)

        def developer_02 = "developer_02"
        saveUser(developer_02)

        def projectId = projectRepository.findAll().stream().findFirst().get().id
        performPatch(ASSIGN_USER_TO_PROJECT, developer_01, projectId)
        performPatch(ASSIGN_USER_TO_PROJECT, developer_02, projectId)
        performPost(SAVE_AND_ASSIGN_TASK_TO_USER, getCreateTaskRequest(projectId, developer_01))
        def taskId = taskRepository.findAll().stream().findFirst().get().id

        when:
        def result = performPatch(REASSIGN_TASK_TO_ANOTHER_USER, taskId, developer_02, projectId)

        then:
        result.andDo(print())
                .andExpect(status().isCreated())

        def task = taskRepository.findByIdWithEagerCreatedByAndAssignedTo(taskId).get()
        task != null
        task.project.id == projectId
        task.createdBy.username == "admin-user"
        task.assignedTo.username == developer_02
    }

    @WithMockUser(username = "admin-user")
    def "Delete task under user and project"() {
        given:
        saveProjectWithCreatedBy("admin-user")

        def developer_01 = "developer_01"
        saveUser(developer_01)

        def projectId = projectRepository.findAll().stream().findFirst().get().id
        performPatch(ASSIGN_USER_TO_PROJECT, developer_01, projectId)
        performPost(SAVE_AND_ASSIGN_TASK_TO_USER, getCreateTaskRequest(projectId, developer_01))
        def taskId = taskRepository.findAll().stream().findFirst().get().id

        when:
        def result = performDelete(DELETE_TASK_BY_ID, taskId)

        then:
        result.andDo(print())
                .andExpect(status().isNoContent())

        taskRepository.findAll().size() == 0
    }

    @WithMockUser(username = "admin-user")
    def "Delete project with tasks under user"() {
        given:
        saveProjectWithCreatedBy("admin-user")

        def developer_01 = "developer_01"
        saveUser(developer_01)
        def id = projectRepository.findAll().stream().findFirst().get().id
        performPatch(ASSIGN_USER_TO_PROJECT, developer_01, id)

        when:
        def result = performDelete(DELETE_PROJECT_BY_ID, id, developer_01) //TODO: delete by username or authentication?

        then:
        result.andDo(print())
                .andExpect(status().isNoContent())

        userRepository.findUserWithEagerProjects(developer_01).get().projects.size() == 0
        projectRepository.findAll().size() == 0

    }

    @WithMockUser(username = "admin-user")
    def "Create comment under user project task"() {
        given:
        saveProjectWithCreatedBy("admin-user")

        def developer_01 = "developer_01"
        saveUser(developer_01)

        def projectId = projectRepository.findAll().stream().findFirst().get().id
        performPatch(ASSIGN_USER_TO_PROJECT, developer_01, projectId)
        performPost(SAVE_AND_ASSIGN_TASK_TO_USER, getCreateTaskRequest(projectId, developer_01))
        def taskId = taskRepository.findAll().stream().findFirst().get().id

        when:
        def result = performPost(SAVE_COMMENT_UNDER_USER_TASK, getSaveCommentRequest(taskId))

        then:
        result.andDo(print())
                .andExpect(status().isCreated())

        taskRepository.findByIdWithEagerComments(taskId).get().comments.size() == 1
    }

    @WithMockUser(username = "admin-user")
    def "Create multiple comments under user project task"() {
        given:
        saveProjectWithCreatedBy("admin-user")

        def developer_01 = "developer_01"
        saveUser(developer_01)

        def projectId = projectRepository.findAll().stream().findFirst().get().id
        performPatch(ASSIGN_USER_TO_PROJECT, developer_01, projectId)
        performPost(SAVE_AND_ASSIGN_TASK_TO_USER, getCreateTaskRequest(projectId, developer_01))
        def taskId = taskRepository.findAll().stream().findFirst().get().id
        performPost(SAVE_COMMENT_UNDER_USER_TASK, getSaveCommentRequest(taskId))
        performPost(SAVE_COMMENT_UNDER_USER_TASK, getSaveCommentRequest(taskId))
        performPost(SAVE_COMMENT_UNDER_USER_TASK, getSaveCommentRequest(taskId))

        when:
        def task = taskRepository.findByIdWithEagerComments(taskId).get()

        then:
        task.comments.size() == 3
        commentRepository.findAll().size() == 3
    }

    @WithMockUser(username = "admin-user")
    def "Delete comment under user project task"() {
        given:
        saveProjectWithCreatedBy("admin-user")

        def developer_01 = "developer_01"
        saveUser(developer_01)

        def projectId = projectRepository.findAll().stream().findFirst().get().id
        performPatch(ASSIGN_USER_TO_PROJECT, developer_01, projectId)
        performPost(SAVE_AND_ASSIGN_TASK_TO_USER, getCreateTaskRequest(projectId, developer_01))
        def taskId = taskRepository.findAll().stream().findFirst().get().id
        performPost(SAVE_COMMENT_UNDER_USER_TASK, getSaveCommentRequest(taskId))
        def commentId = commentRepository.findAll().findAll().stream().findFirst().get().id

        when:
        def result = performDelete(DELETE_COMMENT_UNDER_USER_TASK, commentId)

        then:
        result.andDo(print())
                .andExpect(status().isNoContent())

        taskRepository.findByIdWithEagerComments(taskId).get().comments.size() == 0
    }

    @WithMockUser(username = "admin-user")
    def "Delete task with all comments cascaded"() {
        given:
        saveProjectWithCreatedBy("admin-user")

        def developer_01 = "developer_01"
        saveUser(developer_01)

        def projectId = projectRepository.findAll().stream().findFirst().get().id
        performPatch(ASSIGN_USER_TO_PROJECT, developer_01, projectId)
        performPost(SAVE_AND_ASSIGN_TASK_TO_USER, getCreateTaskRequest(projectId, developer_01))
        def taskId = taskRepository.findAll().stream().findFirst().get().id
        performPost(SAVE_COMMENT_UNDER_USER_TASK, getSaveCommentRequest(taskId))
        performPost(SAVE_COMMENT_UNDER_USER_TASK, getSaveCommentRequest(taskId))
        performPost(SAVE_COMMENT_UNDER_USER_TASK, getSaveCommentRequest(taskId))

        when:
        def result = performDelete(DELETE_TASK_BY_ID, taskId)

        then:
        result.andDo(print())
                .andExpect(status().isNoContent())
        commentRepository.findAll().size() == 0
    }

    @WithMockUser(username = "admin-user")
    def "Delete user with projects tasks"() {
        given:
        def developer_01 = "developer_01"
        saveUser(developer_01)
        saveProjectWithCreatedBy("admin-user")
        saveProjectWithCreatedBy("admin-user")
        saveProjectWithCreatedBy("admin-user")
        for(Project p : projectRepository.findAll()) {
            performPatch(ASSIGN_USER_TO_PROJECT, developer_01, p.id)
            performPost(SAVE_AND_ASSIGN_TASK_TO_USER, getCreateTaskRequest(p.id, developer_01))

        }
        performPatch(LEAVE_ALL_PROJECTS, developer_01)

        when:
        def result = performDelete(DELETE_USER_UNDER_PROJECTS_TASKS, developer_01)

        then:
        result.andDo(print())
                .andExpect(status().isNoContent())

        userRepository.findAll().size() == 1
        taskRepository.findAll().size() == 0
    }

}
