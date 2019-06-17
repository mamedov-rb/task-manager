package unit.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.rmamedov.app.StartApp;
import ru.rmamedov.app.config.LoginAccessDeniedHandler;
import ru.rmamedov.app.controller.api.ProjectController;
import ru.rmamedov.app.model.Project;
import ru.rmamedov.app.service.interfaces.IProjectService;

import java.time.LocalDate;
import java.time.Month;

/**
 * @author Rustam Mamedov
 */

@RunWith(SpringRunner.class)
@WebMvcTest(value = {ProjectController.class, LoginAccessDeniedHandler.class}, secure = false)
@ContextConfiguration(classes = {StartApp.class})
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProjectService projectService;

    private final Project mockProject = new Project("Project_2", "Some description of project #2.",
               LocalDate.of(2019, Month.MARCH, 1), LocalDate.of(2020, Month.OCTOBER, 1));

    private final String projectAsJson = "{\n" +
            "    \"name\": \"Project_2\",\n" +
            "    \"description\": \"Some description of project #2.\",\n" +
            "    \"created\": null,\n" +
            "    \"updated\": null,\n" +
            "    \"startDate\": \"2019-03-01\",\n" +
            "    \"endDate\": \"2020-10-01\",\n" +
            "    \"tasks\": null,\n" +
            "    \"users\": null\n" +
            "}";

    @Test
    public void findAllProjectsAsJsonControllerTest() throws Exception {
        Mockito
                .when(projectService.findById(Mockito.anyString()))
                .thenReturn(mockProject);
        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/project/some-id").accept(MediaType.APPLICATION_JSON);
        final MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        JSONAssert.assertEquals(projectAsJson, result.getResponse().getContentAsString(), false);
    }
}
