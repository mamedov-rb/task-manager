package ru.rmamedov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.taskmanager.exception.UserNotFoundException;
import ru.rmamedov.taskmanager.model.DTO.UserDTO;
import ru.rmamedov.taskmanager.model.Project;
import ru.rmamedov.taskmanager.model.User;
import ru.rmamedov.taskmanager.model.DTO.UserMetaDTO;
import ru.rmamedov.taskmanager.repository.UserRepository;

import java.util.Set;

/**
 * @author Rustam Mamedov
 */

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

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
    public Set<UserMetaDTO> findAllByProjectWithRoles(final Project project) throws UserNotFoundException {
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
