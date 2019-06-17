package ru.rmamedov.app.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.rmamedov.app.StartApp;
import ru.rmamedov.app.exception.ProjectAlreadyExistsException;
import ru.rmamedov.app.exception.ProjectNotFoundException;
import ru.rmamedov.app.model.Project;
import ru.rmamedov.app.service.interfaces.IProjectService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Rustam Mamedov
 *
 * In this test class defined tests for single projectService component.
 * Here no colaboration bitween services.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StartApp.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProjectServiceTest {

    @Autowired
    private IProjectService projectService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private String id1 = null;
    private String id2 = null;

    @Before
    public void init() {
        id1 = projectService
                .saveWithSelfInjection(new Project("Project_1", "Some description of project #1.", LocalDate.parse("2019-07-10"), LocalDate.parse("2020-10-10"))).getId();
        id2 = projectService
                .saveWithSelfInjection(new Project("Project_2", "Some description of project #2.", LocalDate.parse("2019-10-01"), LocalDate.parse("2020-10-10"))).getId();
    }

    @After
    public void destroy() {
        projectService.deleteAll();
    }

    @Test
    public void projectServiceIsExistsTest() {
        assertNotNull(projectService);
    }

    // Find.
    @Test
    public void findProjectByIdTest() {
        final Project project = projectService.findById(id2);
        assertNotNull(project);
        assertEquals("Some description of project #2.", project.getDescription());
    }
    @Test
    public void findNonexistentProjectByIdTest() {
        exceptionRule.expect(ProjectNotFoundException.class);
        exceptionRule.expectMessage("Project with ID: 'projectID_3' - Not found!");
        projectService.findById("projectID_3");
    }
    @Test
    public void findAllProjectsTest() {
        final List<Project> projects = projectService.findAll();
        assertEquals(2, projects.size());
    }

    // save.
    @Test
    @DirtiesContext
    public void saveProjectWithSelfInjectionTest() {
        final Project project = projectService.saveWithSelfInjection(new Project("Project_3", "Some description of project #3.",
                LocalDate.parse("2019-09-02"), LocalDate.parse("2020-10-02")));
        assertEquals(3, projectService.findAll().size());
        assertNotNull(project.getId());
        assertNotNull(project.getCreated());
        assertNotNull(project.getUpdated());
        assertEquals("Project_3", project.getName());
        assertEquals("Some description of project #3.", project.getDescription());
    }
    @Test
    @DirtiesContext
    public void whileSavingProjectWithTheSameNameThenExceptionIsThrowsTest() {
        exceptionRule.expect(ProjectAlreadyExistsException.class);
        exceptionRule.expectMessage("Project with name: 'Project_2' - already exists!");
        projectService.saveWithSelfInjection(new Project("Project_2", "Some description of project #2.",
               LocalDate.parse("2019-09-02"), LocalDate.parse("2020-10-02")));
    }

    // update.
    @Test
    @DirtiesContext
    public void fetchProjectTest() {
        final Project project = projectService.findById(id2);
        project.setDescription("Bla - Bla - Bla");
        project.setStartDate(LocalDate.parse("2019-11-20"));
        final Project updated = projectService.patch(project);

        assertNotNull(updated.getUpdated());
        assertEquals("Project_2", updated.getName());
        assertEquals("Bla - Bla - Bla", updated.getDescription());
        assertEquals(LocalDate.parse("2019-11-20"), updated.getStartDate());
    }
    @Test
    @DirtiesContext
    public void updateProjectTest() {
        final Project project = projectService.findById(id2);
        project.setDescription("Bla - Bla - Bla");
        project.setStartDate(LocalDate.parse("2019-11-20"));
        final Project updated = projectService.update(project);

        assertNotNull(updated.getUpdated());
        assertEquals("Project_2", updated.getName());
        assertEquals("Bla - Bla - Bla", updated.getDescription());
        assertEquals(LocalDate.parse("2019-11-20"), updated.getStartDate());
    }

    // Delete.
    @Test
    @DirtiesContext
    public void deleteProjectTest() {
        projectService.deleteById(id1);
        assertEquals(1, projectService.findAll().size());
    }
    @Test
    @DirtiesContext
    public void deleteAllProjectsTest() {
        exceptionRule.expect(ProjectNotFoundException.class);
        exceptionRule.expectMessage("There is no any projects!");
        projectService.deleteAll();
        assertEquals(0, projectService.findAll().size());
    }
}
