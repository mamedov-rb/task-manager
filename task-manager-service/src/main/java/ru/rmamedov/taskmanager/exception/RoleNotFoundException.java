package ru.rmamedov.taskmanager.exception;

/**
 * @author Rustam Mamedov
 */

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(String message) {
        super(message);
    }
}
