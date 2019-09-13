package ru.rmamedov.taskmanager.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rmamedov.taskmanager.model.Role;

import java.util.Optional;

/**
 * @author Rustam Mamedov
 */

@Repository
public interface IRoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByName(@NotNull final String name);

}
