package ru.rmamedov.app.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.rmamedov.app.model.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProjectRepository extends JpaRepository<Project, String> {

    String ALL_PROJECTS_OF_CURRENT_USER =
                    "select\n" +
                    "       *\n" +
                    "from project p\n" +
                    "    left join users_projects up on p.id = up.project_id\n" +
                    "where up.user_id = (:id)";

    @Query(nativeQuery = true, value = "SELECT * FROM project ORDER BY created ASC")
    List<Project> findAllSortByCreationDate();

    @Query(nativeQuery = true, value = "SELECT * FROM project ORDER BY start_date ASC")
    List<Project> findAllSortByStartDate();

    @Query(nativeQuery = true, value = "SELECT * FROM project ORDER BY end_date ASC")
    List<Project> findAllSortByEndDate();

    @Query(nativeQuery = true, value = "SELECT * FROM project p WHERE (p.name LIKE CONCAT('%',:string,'%')) OR (p.description LIKE CONCAT('%',:string,'%'))")
    List<Project> findAnyByNameOrDesc(@Param("string") String string);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.tasks WHERE p.id = (:id)")
    Optional<Project> findByIdWithEagerTasks(@Param("id") String id);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.users WHERE p.id = (:id)")
    Optional<Project> findByIdWithEagerUsers(@Param("id") String id);

    @Query(nativeQuery = true, value = ALL_PROJECTS_OF_CURRENT_USER)
    Optional<List<Project>> findAllOfCurrentUser(@Param("id") @NotNull final String id);

    @Query(value = "SELECT p FROM Project p")
    Optional<List<Project>> findAllProjects();
}
