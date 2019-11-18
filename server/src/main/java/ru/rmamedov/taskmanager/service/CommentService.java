package ru.rmamedov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.rmamedov.taskmanager.exception.CommentNotFoundException;
import ru.rmamedov.taskmanager.model.Comment;
import ru.rmamedov.taskmanager.model.DTO.CommentProjection;
import ru.rmamedov.taskmanager.repository.CommentRepository;

import java.util.Set;

/**
 * @author Rustam Mamedov
 */

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Set<CommentProjection> findAll(final String taskId) {
        return commentRepository.findAllByTaskId(taskId);
    }

    @NotNull
    public Comment findByIdWithEagerCommentatorAndTask(final String id) {
        return commentRepository.findByIdWithEagerCommentatorAndTask(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id: '" + id + "' - Not found."));
    }

    public Comment save(final Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteById(final String id) {
        commentRepository.deleteById(id);
    }
}
