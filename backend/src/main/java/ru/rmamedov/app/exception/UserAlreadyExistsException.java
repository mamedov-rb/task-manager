package ru.rmamedov.app.exception;

/**
 * @author Rustam Mamedov
 */

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
