package ru.rmamedov.taskmanager.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rmamedov.taskmanager.model.DTO.UserQuickPreview;
import ru.rmamedov.taskmanager.model.Project;
import ru.rmamedov.taskmanager.model.User;
import ru.rmamedov.taskmanager.model.DTO.UserPreviewDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Rustam Mamedov
 */

public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT new ru.rmamedov.taskmanager.model.DTO.UserQuickPreview(u.id, u.firstName, u.lastName) FROM User u " +
            "WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :param, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :param, '%'))")
    List<UserQuickPreview> getPreview(
            @Param("param") String param,
            Pageable pageable
    );

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username = :username AND :project MEMBER OF u.projects")
    Optional<User> findByUsernameAndProject(@Param("username") String username, @Param("project") Project project);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE :project MEMBER OF u.projects")
    Set<UserPreviewDTO> findAllByProjectWithEagerRolesAsProjection(@Param("project") Project project);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.projects WHERE u.username = :username")
    Optional<User> findUserWithEagerProjects(@Param("username") String username);

}
