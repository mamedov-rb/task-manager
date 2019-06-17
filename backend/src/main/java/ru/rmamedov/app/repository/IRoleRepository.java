package ru.rmamedov.app.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.rmamedov.app.model.user.Role;

import java.util.Optional;

/**
 * @author Rustam Mamedov
 */

@Repository
public interface IRoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByName(@NotNull final String name);

    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.users WHERE r.name = (:name)")
    Optional<Role> findByNameWithEagerUsers(@Param("name") @NotNull final String name);
}
