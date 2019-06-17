package ru.rmamedov.app.exception;

/**
 * @author Rustam Mamedov
 */

public class RoleAlreadyExistsException extends RuntimeException {

    public RoleAlreadyExistsException(String message) {
        super(message);
    }
}
