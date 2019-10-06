package ru.rmamedov.taskmanager.exception;

/**
 * @author Rustam Mamedov
 */

public class TaskAlreadyExistsException extends RuntimeException {

    public TaskAlreadyExistsException(String message) {
        super(message);
    }
}
