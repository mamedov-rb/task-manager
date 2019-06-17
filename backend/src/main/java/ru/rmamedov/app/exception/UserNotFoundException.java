package ru.rmamedov.app.exception;

/**
 * @author Rustam Mamedov
 */

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
