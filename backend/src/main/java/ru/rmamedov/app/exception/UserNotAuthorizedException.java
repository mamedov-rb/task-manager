package ru.rmamedov.app.exception;

/**
 * @author Rustam Mamedov
 */

public class UserNotAuthorizedException extends RuntimeException {

    public UserNotAuthorizedException(String message) {
        super(message);
    }
}
