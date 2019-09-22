package ru.rmamedov.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.rmamedov.taskmanager.model.Comment;

import java.util.Optional;

/**
 * @author Rustam Mamedov
 */

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    @Query("SELECT c FROM Comment c JOIN FETCH c.commentator JOIN FETCH c.task WHERE c.id = :id")
    Optional<Comment> findByIdWithEagerCommentatorAndTask(@Param("id") String id);

}
