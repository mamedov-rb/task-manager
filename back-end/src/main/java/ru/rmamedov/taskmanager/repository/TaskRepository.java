package ru.rmamedov.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.rmamedov.taskmanager.model.Comment;
import ru.rmamedov.taskmanager.model.DTO.TaskProjection;
import ru.rmamedov.taskmanager.model.Project;
import ru.rmamedov.taskmanager.model.Task;
import ru.rmamedov.taskmanager.model.User;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    Set<Task> findAllByAssignedToAndProject(User assignedTo, Project project);

    Set<TaskProjection> findAllByAssignedToUsernameAndProjectId(String assignedTo, String projectId);

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
