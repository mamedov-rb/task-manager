package ru.rmamedov.app.exception;

/**
 * @author Rustam Mamedov
 */

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(String message) {
        super(message);
    }
}
