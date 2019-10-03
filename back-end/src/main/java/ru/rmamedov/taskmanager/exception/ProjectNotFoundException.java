package ru.rmamedov.taskmanager.exception;

/**
 * @author Rustam Mamedov
 */

public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException(String message) {
        super(message);
    }
}
