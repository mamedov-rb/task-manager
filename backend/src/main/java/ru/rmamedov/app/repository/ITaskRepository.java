package ru.rmamedov.app.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.rmamedov.app.model.Task;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITaskRepository extends JpaRepository<Task, String> {

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.project WHERE t.id = (:id)")
    Optional<Task> findByIdWithEagerProject(@Param("id") @NotNull final String id);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.user WHERE t.id = (:id)")
    Optional<Task> findByIdWithEagerUser(@Param("id") @NotNull final String id);

    @Query(nativeQuery = true, value = "SELECT * FROM task t WHERE t.project_id = :id ORDER BY t.created ASC")
    List<Task> findAllByProjectOrderByCreatedAsc(@Param("id") @NotNull final String id);

    @Query(nativeQuery = true, value = "SELECT * FROM task t WHERE t.project_id = :id ORDER BY t.start_date ASC")
    List<Task> findAllByProjectOrderByStartDateAsc(@Param("id") @NotNull final String id);

    @Query(nativeQuery = true, value = "SELECT * FROM task t WHERE t.project_id = :id ORDER BY t.end_date ASC")
    List<Task> findAllByProjectOrderByEndDateAsc(@Param("id") @NotNull final String id);

    @Query(nativeQuery = true, value = "SELECT * FROM task t WHERE t.project_id = :id")
    List<Task> findAllByProjectId(@Param("id") @NotNull final String id);
}
