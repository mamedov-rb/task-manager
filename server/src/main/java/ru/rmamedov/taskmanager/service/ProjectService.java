package ru.rmamedov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.taskmanager.exception.ProjectNotFoundException;
import ru.rmamedov.taskmanager.exception.UserNotAuthorizedException;
import ru.rmamedov.taskmanager.model.DTO.ProjectDetailsProjection;
import ru.rmamedov.taskmanager.model.DTO.ProjectProjection;
import ru.rmamedov.taskmanager.model.Project;
import ru.rmamedov.taskmanager.model.User;
import ru.rmamedov.taskmanager.repository.ProjectRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final UserService userService;

    List<ProjectProjection> getPreview(final String name, final Pageable pageable) {
        return projectRepository.getPreview(name, pageable);
    }

    @Transactional
    public Project save(@NotNull final Project project) throws UserNotAuthorizedException {
        return projectRepository.save(project);
    }

    @NotNull
    public Project findById(final String id) throws ProjectNotFoundException {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + id + " - Not found."));
    }

    @NotNull
    @Transactional(readOnly = true)
    public ProjectDetailsProjection findDTOById(final String id) throws ProjectNotFoundException {
        return projectRepository.findDetailsAsProjection(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + id + " - Not found."));
    }

    @NotNull
    public Project findByIdWithEagerUsers(final String id) throws ProjectNotFoundException {
        return projectRepository.findByIdWithEagerUsers(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id: " + id + " - Not found."));
    }

    @NotNull
    public Set<ProjectProjection> findAllOfCurrentUser(@Nullable final Authentication authentication) {
        if (authentication == null) {
            throw new UserNotAuthorizedException("User - Not authorized. Please login");
        }
        final User user = (User) userService.loadUserByUsername(authentication.getName());
        return projectRepository.findAllAsProjectionByUsername(user);
    }

    public void deleteProjectById(final String id) {
        projectRepository.deleteById(id);
    }

    public boolean isMemberOf(@Nullable final Authentication authentication) {
        if (authentication == null) {
            throw new UserNotAuthorizedException("User - Not authorized. Please login");
        }
        final User user = (User) userService.loadUserByUsername(authentication.getName());
        return !projectRepository.findByUser(user).isEmpty();
    }

}
