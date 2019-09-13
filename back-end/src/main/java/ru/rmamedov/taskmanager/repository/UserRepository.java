package ru.rmamedov.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rmamedov.taskmanager.model.User;

import java.util.Optional;

/**
 * @author Rustam Mamedov
 */

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

}
