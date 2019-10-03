package ru.rmamedov.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.rmamedov.taskmanager.exception.CommentNotFoundException;
import ru.rmamedov.taskmanager.model.Comment;
import ru.rmamedov.taskmanager.repository.CommentRepository;

/**
 * @author Rustam Mamedov
 */

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

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
