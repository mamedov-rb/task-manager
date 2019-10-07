package ru.rmamedov.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.rmamedov.taskmanager.model.DTO.ProjectDetailsProjection;
import ru.rmamedov.taskmanager.model.DTO.ProjectProjection;
import ru.rmamedov.taskmanager.model.Project;
import ru.rmamedov.taskmanager.model.User;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    @Query("SELECT p FROM Project p WHERE :user MEMBER OF p.users OR p.createdBy = :user")
    Set<ProjectProjection> findAllAsProjectionByUsername(@Param("user") User user);

    @Query("SELECT p FROM Project p WHERE :user MEMBER OF p.users")
    Optional<Project> findByUser(@Param("user") User user);

    @Query("SELECT p FROM Project p WHERE p.id = :id")
    Optional<ProjectDetailsProjection> findDetailsAsProjection(@Param("id")String id);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.users WHERE p.id = :id")
    Optional<Project> findByIdWithEagerUsers(@Param("id") String id);

}
