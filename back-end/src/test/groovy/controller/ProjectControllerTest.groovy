package controller

import helper.MockMvcHelper
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.ResultActions
import ru.rmamedov.taskmanager.model.DTO.ProjectDTO
import ru.rmamedov.taskmanager.model.Project
import ru.rmamedov.taskmanager.model.User

import static TestData.getProject
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

/**
 * @author Rustam Mamedov
 */

class ProjectControllerTest extends MockMvcHelper {

    @MockBean
    SecurityContext securityContext

    @MockBean
    Authentication authentication

    def cleanup() {
        clear()
    }

    @WithMockUser(username = "test-user")
    def "Create new project"() {
        given:
        performSavingUser()

        when:
        def result = performPost(CREATE_PROJECT, getProject())

        then:
        result.andDo(print())
                .andExpect(status().isCreated())
        userRepository.findAll().size() == 1
    }

    @WithMockUser
    def "Create new project if 'createdBy' Not found."() {
        when:
        def result = performPost(CREATE_PROJECT, getProject())

        then:
        result.andDo(print())
                .andExpect(status().isBadRequest())
    }

    @WithMockUser(username = "test-user")
    def "Find project by id"() {
        given:
        saveProjectWithCreatedBy()
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
    }

//    @WithMockUser(username = "test-user")
//    def "Assign project to user"() {
//        given:
//        saveProjectWithCreatedBy()
//        def id = projectRepository.findAll().stream().findFirst().get().id
//
//        when:
//        def result = performPatch(ASSIGN_TO_USER, id)
//
//        then:
//        result.andDo(print())
//                .andExpect(status().isCreated())
//        projectRepository.findAllProjectsByUsername("test-user").size() == 1
//        userRepository.findUserWithEagerProjects("test-user").get().projects.size() == 1
//        projectRepository.findProjectWithEagerUsers(id).get().users.size() == 1
//
//        cleanup:
//        def user = userRepository.findUserWithEagerProjects("test-user").get()
//        def project = projectRepository.findById(id).get()
//        user.removeProject(project)
//        userRepository.findAll()
//    }

//    @WithMockUser(username = "test-user") // TODO: fix
//    def "Find all by current user"() {
//        given:
//        saveProjectWithCreatedBy()
//
//        when:
//        def result = performGet(FIND_ALL_PROJECTS_BY_USER_ID, "test-user")
//
//        then:
//        result.andDo(print())
//                .andExpect(status().isOk())
//    }


    def "Create new project - 403"() {
        when:
        def result = performPost(CREATE_PROJECT, getProject())

        then:
        result.andDo(print())
                .andExpect(status().isForbidden())
    }

    def "Find project by id - 403"() {
        when:
        def result = performGet(FIND_PROJECT_BY_ID, "123")

        then:
        result.andDo(print())
                .andExpect(status().isForbidden())
    }

}
