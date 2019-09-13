package ru.rmamedov.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rmamedov.taskmanager.model.User;

/**
 * @author Rustam Mamedov
 */

public interface IUserRepository extends JpaRepository<User, String> {

}
