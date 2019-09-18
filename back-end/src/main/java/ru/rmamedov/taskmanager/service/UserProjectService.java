package ru.rmamedov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.taskmanager.model.Project;
import ru.rmamedov.taskmanager.model.User;

/**
 * @author Rustam Mamedov
 */

@Service
@RequiredArgsConstructor
public class UserProjectService {

    private final UserService userService;

    private final ProjectService projectService;

    @Transactional
    public boolean assignUserToProject(final String username, final String projectId) {
        final User user = userService.findByUsernameWithEagerProject(username);
        final Project project = projectService.findByIdWithEagerUsers(projectId);
        return user.addProject(project);
    }

}
