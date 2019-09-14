package ru.rmamedov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rmamedov.taskmanager.model.User;
import ru.rmamedov.taskmanager.model.projection.UserDTO;
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

    @Transactional
    public UserDTO create(final User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return UserDTO.of(userRepository.save(user));
    }

}
