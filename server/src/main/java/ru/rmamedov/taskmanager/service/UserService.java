package ru.rmamedov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.taskmanager.exception.UserNotFoundException;
import ru.rmamedov.taskmanager.model.DTO.UserDTO;
import ru.rmamedov.taskmanager.model.DTO.UserPreviewDTO;
import ru.rmamedov.taskmanager.model.DTO.UserQuickPreview;
import ru.rmamedov.taskmanager.model.Project;
import ru.rmamedov.taskmanager.model.User;
import ru.rmamedov.taskmanager.repository.UserRepository;

import java.util.List;
import java.util.Set;

/**
 * @author Rustam Mamedov
 */

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    List<UserQuickPreview> getPreview(final String param, final Pageable pageable) {
        return userRepository.getPreview(param, pageable);
    }

    @NotNull
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: '" + username + "' - Not found!"));
    }

    @NotNull
    public User findByUsernameAndProject(final String username, final Project project) throws UserNotFoundException {
        return userRepository.findByUsernameAndProject(username, project)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: '" + username +
                        "' - Not found, or user not a member of project with name: '" + project.getName() + "'."));

    }

    @NotNull
    public Set<UserPreviewDTO> findAllByProjectWithRoles(final Project project) throws UserNotFoundException {
        return userRepository.findAllByProjectWithEagerRolesAsProjection(project);
    }

    @NotNull
    public User findByUsernameWithEagerProject(final String username) {
        return userRepository.findUserWithEagerProjects(username)
                .orElseThrow(() -> new UserNotFoundException("User with username: '" + username + "' - Not found!"));
    }

    @NotNull
    public UserDTO findByUsername(final String username) {
        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username: '" + username + "' - Not found!"));
        return UserDTO.of(user);
    }

    @Transactional
    public void save(final User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void deleteByUsername(final String username) {
        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: '" + username + "' - Not found!"));
        userRepository.delete(user);
    }

}
