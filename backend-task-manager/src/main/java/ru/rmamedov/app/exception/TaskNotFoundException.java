package ru.rmamedov.app.exception;

/**
 * @author Rustam Mamedov
 */

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String message) {
        super(message);
    }
}
