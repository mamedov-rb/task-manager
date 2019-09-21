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
//        clear()
//    }

    @WithMockUser(username = "admin-user")
    def "Assign project to user"() {
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

        // TODO: leave project before cleanup()
    }

    @WithMockUser(username = "admin-user")
    def "Assign task to user and project"() {
        given:
        def developerUsername = "developer-user"
        saveProjectWithCreatedBy("admin-user")
        saveUser(developerUsername)
        def projectId = projectRepository.findAll().stream().findFirst().get().id
        def request = getCreateTaskRequest(projectId, developerUsername)
        performPatch(ASSIGN_TO_PROJECT, developerUsername, projectId)

        when:
        def result = performPost(ASSIGN_TASK_TO_USER, request)

        then:
        result.andDo(print())
                .andExpect(status().isCreated())
        def id = taskRepository.findAll().stream().findFirst().get().id
        def task = taskRepository.findByIdWithEagerCreatedByAndAssignedTo(id).get()
        task != null
        task.project.id == projectId
        task.createdBy.username == "admin-user"
        task.assignedTo.username == developerUsername

        // TODO: leave project before cleanup()
    }



}
