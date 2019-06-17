package ru.rmamedov.app.exception;

/**
 * @author Rustam Mamedov
 */

public class TaskAlreadyExistsException extends RuntimeException {

    public TaskAlreadyExistsException(String message) {
        super(message);
    }
}
