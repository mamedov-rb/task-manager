package ru.rmamedov.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.rmamedov.taskmanager.model.DTO.ProjectDTO;
import ru.rmamedov.taskmanager.model.Project;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    @Query("SELECT p FROM Project p JOIN p.users u WHERE u.username = :username")
    Set<ProjectDTO> findAllProjectsByUsername(@Param("username") String username);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.users WHERE p.id = :id")
    Optional<Project> findProjectWithEagerUsers(@Param("id") String id);

}
