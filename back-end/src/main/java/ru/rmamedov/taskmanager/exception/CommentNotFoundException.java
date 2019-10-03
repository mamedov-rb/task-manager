package ru.rmamedov.taskmanager.exception;

/**
 * @author Rustam Mamedov
 */

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(String message) {
        super(message);
    }
}
