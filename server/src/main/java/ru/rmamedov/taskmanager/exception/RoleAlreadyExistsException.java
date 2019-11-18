package ru.rmamedov.taskmanager.exception;

/**
 * @author Rustam Mamedov
 */

public class RoleAlreadyExistsException extends RuntimeException {

    public RoleAlreadyExistsException(String message) {
        super(message);
    }
}
