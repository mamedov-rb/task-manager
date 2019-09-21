package ru.rmamedov.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.rmamedov.taskmanager.model.Task;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    @Query("SELECT t FROM Task t JOIN FETCH t.assignedTo WHERE t.id = :id")
    Optional<Task> findByIdWithEagerAssignedTo(@Param("id") String id);

    @Query("SELECT t FROM Task t JOIN FETCH t.createdBy JOIN FETCH t.assignedTo WHERE t.id = :id")
    Optional<Task> findByIdWithEagerCreatedByAndAssignedTo(@Param("id") String id);

}
