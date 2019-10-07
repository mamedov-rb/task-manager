package ru.rmamedov.taskmanager.exception;

/**
 * @author Rustam Mamedov
 */

public class ProjectAlreadyExistsException extends RuntimeException {

    public ProjectAlreadyExistsException(String message) {
        super(message);
    }
}
