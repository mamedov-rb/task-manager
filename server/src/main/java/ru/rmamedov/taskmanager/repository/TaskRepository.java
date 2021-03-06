package ru.rmamedov.taskmanager.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.rmamedov.taskmanager.model.Comment;
import ru.rmamedov.taskmanager.model.DTO.TaskDetailsProjection;
import ru.rmamedov.taskmanager.model.DTO.TaskPreviewProjection;
import ru.rmamedov.taskmanager.model.DTO.TaskProjection;
import ru.rmamedov.taskmanager.model.Project;
import ru.rmamedov.taskmanager.model.Task;
import ru.rmamedov.taskmanager.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    @Query("SELECT t FROM Task t " +
            "WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :param, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :param, '%'))")
    List<TaskPreviewProjection> getPreview(
            @Param("param") String name,
            Pageable pageable
    );

    @Query("SELECT t FROM Task t WHERE t.id = :id")
    Optional<TaskDetailsProjection> getDetails(@Param("id") String id);

    Set<Task> findAllByAssignedToAndProject(User assignedTo, Project project);

    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId")
    Set<TaskProjection> findAllByProject(@Param("projectId") String projectId);

    long deleteAllByAssignedToAndProject(User assignedTo, Project project);

    @Query("SELECT t FROM Task t JOIN FETCH t.assignedTo WHERE t.id = :id")
    Optional<Task> findByIdWithEagerAssignedTo(@Param("id") String id);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.comments WHERE t.id = :id")
    Optional<Task> findByIdWithEagerComments(@Param("id") String id);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.comments WHERE :comment MEMBER OF t.comments")
    Optional<Task> findByCommentWithEagerComments(@Param("comment") Comment comment);

    @Query("SELECT t FROM Task t JOIN FETCH t.createdBy JOIN FETCH t.assignedTo WHERE t.id = :id")
    Optional<Task> findByIdWithEagerCreatedByAndAssignedTo(@Param("id") String id);

}
