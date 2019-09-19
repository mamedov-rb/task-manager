import org.apache.commons.lang3.RandomStringUtils
import ru.rmamedov.taskmanager.model.DTO.CreationTaskRequest
import ru.rmamedov.taskmanager.model.Project
import ru.rmamedov.taskmanager.model.Task
import ru.rmamedov.taskmanager.model.User
import ru.rmamedov.taskmanager.model.enums.Status

import java.time.LocalDateTime

/**
 * @author Rustam Mamedov
 */

class TestData {

    static User getUser(String username) {
        User.builder()
                .firstName(RandomStringUtils.randomAlphabetic(10))
                .lastName(RandomStringUtils.randomAlphabetic(10))
                .username(username)
                .password(RandomStringUtils.randomAlphabetic(10))
                .email("user@gmail.com")
                .phone("+7(800)100-10-10")
                .build()
    }

    static Project getProject() {
        Project.builder()
                .name(RandomStringUtils.randomAlphabetic(10))
                .description(RandomStringUtils.randomAlphabetic(10))
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusMonths(1))
                .build()
    }

    static CreationTaskRequest getCreateTaskRequest(String projectId,
                                                    String assignTo) {

        def task = Task.builder()
                .name(RandomStringUtils.randomAlphabetic(10))
                .description(RandomStringUtils.randomAlphabetic(50))
                .status(Status.PLANNED)
                .startDate(LocalDateTime.now().plusDays(10))
                .endDate(LocalDateTime.now().plusDays(30))
                .build()
        new CreationTaskRequest(task, projectId, assignTo)
    }
}
