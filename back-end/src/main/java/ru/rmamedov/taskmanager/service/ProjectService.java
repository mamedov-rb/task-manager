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
    public void save(@NotNull final Project project,
                     @Nullable final Authentication authentication) throws UserNotAuthorizedException {

        if (authentication == null) {
            throw new UserNotAuthorizedException("User - Not authorized. Please login");
        }
        final User createdBy = (User) userService.loadUserByUsername(authentication.getName());
        project.setCreatedBy(createdBy);
        projectRepository.save(project);

    }

    @NotNull
    public Project findById(final String id) throws ProjectNotFoundException {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + id + " - Not found."));
    }

    @NotNull
    @Transactional(readOnly = true)
    public ProjectDTO findDTOById(final String id) throws ProjectNotFoundException {
        final Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + id + " - Not found."));
        return ProjectDTO.of(project);
    }

    @NotNull
    public Project findByIdWithEagerUsers(final String id) throws ProjectNotFoundException {
        return projectRepository.findByIdWithEagerUsers(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + id + " - Not found."));
    }

    @NotNull
    public Set<ProjectDTO> findAllOfCurrentUser(final String username) {
        return projectRepository.findAllOfUserByUsernameAsDto(username);
    }

    public void deleteProjectById(final String id) {
        projectRepository.deleteById(id);
    }

}
