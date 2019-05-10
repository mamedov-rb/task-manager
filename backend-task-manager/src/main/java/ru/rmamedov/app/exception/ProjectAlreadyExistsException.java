package ru.rmamedov.app.exception;

/**
 * @author Rustam Mamedov
 */

public class ProjectAlreadyExistsException extends RuntimeException {

    public ProjectAlreadyExistsException(String message) {
        super(message);
    }
}
