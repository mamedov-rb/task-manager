package unit.service;

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
import ru.rmamedov.app.model.Status;
import ru.rmamedov.app.service.interfaces.IProjectService;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Rustam Mamedov
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StartApp.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProjectServiceTest {

    @Autowired
    private IProjectService projectService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void projectServiceIsExistsTest() {
        assertNotNull(projectService);
    }

    // Save.
    @Test
    @DirtiesContext
    public void saveProjectTest() {
        final Project project = projectService.save(new Project("Project_3", "Some description of project #3.",
                Status.PLANNED, LocalDate.of(2019, Month.SEPTEMBER, 2), LocalDate.of(2020, Month.OCTOBER, 2)));
        final Project saved = projectService.findById(project.getId());
        assertEquals("Project_3", saved.getName());
        assertEquals("Some description of project #3.", saved.getDescription());
        assertEquals(3, projectService.findAll().size());
        assertNotNull(saved.getId());
        assertNotNull(saved.getCreated());
        assertNotNull(saved.getUpdated());
    }
    @Test
    @DirtiesContext
    public void saveProjectWithTheSameNameTest() {
        exceptionRule.expect(ProjectAlreadyExistsException.class);
        exceptionRule.expectMessage("Project with name: 'Project_2' - already exists!");
        projectService.saveWithSelfInjection(new Project("Project_2", "Some description of project #2.",
                Status.PLANNED, LocalDate.of(2019, Month.SEPTEMBER, 2), LocalDate.of(2020, Month.OCTOBER, 2)));
    }
    // Update.
    @Test
    @DirtiesContext
    public void fetchProjectTest() {
        final Project project = projectService.findById("projectID_2");
        project.setDescription("Bla - Bla - Bla");
        project.setStartDate(LocalDate.of(2019, Month.NOVEMBER, 20));
        final Project updated = projectService.patch(project);

        assertEquals("Project_2", updated.getName());
        assertEquals("Bla - Bla - Bla", updated.getDescription());
        assertEquals(LocalDate.of(2019, Month.NOVEMBER, 20), updated.getStartDate());
    }
    @Test
    @DirtiesContext
    public void updateProjectTest() {
        final Project project = projectService.findById("projectID_2");
        project.setDescription("Bla - Bla - Bla");
        project.setStartDate(LocalDate.of(2019, Month.NOVEMBER, 20));
        final Project updated = projectService.update(project);

        assertEquals("Project_2", updated.getName());
        assertEquals("Bla - Bla - Bla", updated.getDescription());
        assertEquals(LocalDate.of(2019, Month.NOVEMBER, 20), updated.getStartDate());
    }

    // Delete.
    @Test
    @DirtiesContext
    public void deleteProjectTest() {
        projectService.deleteById("projectID_2");
        assertEquals(1, projectService.findAll().size());
    }
    @Test
    @DirtiesContext
    public void deleteAllProjectTest() {
        exceptionRule.expect(ProjectNotFoundException.class);
        exceptionRule.expectMessage("There is no any projects!");
        projectService.deleteAll();
        assertEquals(0, projectService.findAll().size());
    }

    // Find.
    @Test
    public void findProjectByIdTest() {
        final Project project = projectService.findById("projectID_2");
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
    // Find sorted.
    @Test
    public void findAllProjectsWithTasksSortedByStartDateTest() {
        final List<Project> projects = projectService.findAllWithEagerTasksSortByStartDate();

        assertEquals(2, projects.size());
        final Project project1 = projects.get(0);
        final Project project2 = projects.get(1);

        assertEquals(LocalDate.of(2019, Month.MARCH, 1), project1.getStartDate());
        assertEquals(LocalDate.of(2019, Month.JULY, 10), project2.getStartDate());
        assertEquals(3, project2.getTasks().size());
    }

}
