package ru.rmamedov.app.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rmamedov.app.model.user.User;

import java.util.Optional;

/**
 * @author Rustam Mamedov
 */

public interface IUserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = (:username)")
    Optional<User> loadByUsernameWithEagerRoles(@Param("username") @NotNull final String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = (:username)")
    Optional<User> findByUsernameWithEagerRoles(@Param("username") @NotNull final String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.projects WHERE u.username = (:username)")
    Optional<User> findByUsernameWithEagerProjects(@Param("username") @NotNull final String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.tasks WHERE u.username = (:username)")
    Optional<User> findByUsernameWithEagerTasks(@Param("username") @NotNull final String username);
}
