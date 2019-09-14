package ru.rmamedov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.taskmanager.exception.ProjectNotFoundException;
import ru.rmamedov.taskmanager.exception.UserNotAuthorizedException;
import ru.rmamedov.taskmanager.model.DTO.ProjectDTO;
import ru.rmamedov.taskmanager.model.Project;
import ru.rmamedov.taskmanager.model.User;
import ru.rmamedov.taskmanager.repository.ProjectRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final UserService userService;

    @Transactional
    public void create(@NotNull final Project project, @Nullable final Authentication authentication) {
        if (authentication != null) {
            final User user = (User) userService.loadUserByUsername(authentication.getName());
            project.setCreatedBy(user);
            projectRepository.save(project);
        } else {
            throw new UserNotAuthorizedException("User - Not authorized. Please login");
        }
    }

    @Transactional(readOnly = true)
    public ProjectDTO findById(final String id) {
        final Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + id + " - Not found."));
        return ProjectDTO.of(project);
    }

    @Transactional(readOnly = true)
    public Set<ProjectDTO> findAllByUsername(final String username) {
        return projectRepository.findAllProjectsByUsername(username);
    }

    @Transactional
    public void assignToUserById(final String id, @Nullable final Authentication authentication) {
//        if (authentication != null) {
//            final User user = (User) userService.loadUserByUsername(authentication.getName());
//            final Project project = projectRepository.findById(id)
//                    .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + id + " - Not found."));
//            user.addProject(project);
//        } else {
//            throw new UserNotAuthorizedException("User - Not authorized. Please login");
//        }
    }

}
