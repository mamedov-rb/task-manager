package controller

import helper.MockMvcHelper
import org.springframework.security.test.context.support.WithMockUser

import static TestData.getCreateTaskRequest
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author Rustam Mamedov
 */

class ProjectManagerControllerTest extends MockMvcHelper {

//    def cleanup() {
//        clearDb()
//    }

    @WithMockUser(username = "admin-user")
    def "Assign user to project"() {
        given:
        def developerUsername = "developer-user"
        saveProjectWithCreatedBy("admin-user")
        saveUser(developerUsername)
        def id = projectRepository.findAll().stream().findFirst().get().id

        when:
        def result = performPatch(ASSIGN_TO_PROJECT, developerUsername, id)

        then:
        result.andDo(print())
                .andExpect(status().isCreated())

        userRepository.findUserWithEagerProjects(developerUsername).get().projects.size() == 1
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
        performPatch(ASSIGN_TO_PROJECT, developer_01, id)
        performPatch(ASSIGN_TO_PROJECT, developer_02, id)
        performPatch(ASSIGN_TO_PROJECT, developer_03, id)

        when:
        def project = projectRepository.findByIdWithEagerUsers(id).get()

        then:
        project.users.size() == 3
        userRepository.findUserWithEagerProjects(developer_01).get().projects.size() == 1
        userRepository.findUserWithEagerProjects(developer_02).get().projects.size() == 1
        userRepository.findUserWithEagerProjects(developer_03).get().projects.size() == 1
    }

    @WithMockUser(username = "admin-user")
    def "Create and assign task to user and project"() {
        given:
        def developer = "developer-user"
        saveProjectWithCreatedBy("admin-user")
        saveUser(developer)
        def projectId = projectRepository.findAll().stream().findFirst().get().id
        performPatch(ASSIGN_TO_PROJECT, developer, projectId)

        when:
        def result = performPost(CREATE_AND_ASSIGN_TASK_TO_USER, getCreateTaskRequest(projectId, developer))

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
        performPatch(ASSIGN_TO_PROJECT, developer_01, projectId)
        performPatch(ASSIGN_TO_PROJECT, developer_02, projectId)
        performPost(CREATE_AND_ASSIGN_TASK_TO_USER, getCreateTaskRequest(projectId, developer_01))
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
        performPatch(ASSIGN_TO_PROJECT, developer_01, projectId)
        performPost(CREATE_AND_ASSIGN_TASK_TO_USER, getCreateTaskRequest(projectId, developer_01))
        def taskId = taskRepository.findAll().stream().findFirst().get().id

        when:
        def result = performDelete(DELETE_TASK_BY_ID, taskId)

        then:
        result.andDo(print())
                .andExpect(status().isNoContent())

        taskRepository.findAll().size() == 0
    }

}
