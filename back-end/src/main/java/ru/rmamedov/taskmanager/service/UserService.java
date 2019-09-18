package ru.rmamedov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.taskmanager.exception.UserNotFoundException;
import ru.rmamedov.taskmanager.model.DTO.UserDTO;
import ru.rmamedov.taskmanager.model.User;
import ru.rmamedov.taskmanager.repository.UserRepository;

/**
 * @author Rustam Mamedov
 */

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " - Not found!"));
    }

    public User findByUsernameWithEagerProject(final String username) {
        return userRepository.findUserWithEagerProjects(username)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " - Not found!"));
    }

    public UserDTO findByUsername(final String username) {
        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " - Not found!"));
        return UserDTO.of(user);
    }

    @Transactional
    public void save(final User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

}
