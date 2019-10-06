package ru.rmamedov.taskmanager.exception;

/**
 * @author Rustam Mamedov
 */

public class UserNotAuthorizedException extends RuntimeException {

    public UserNotAuthorizedException(String message) {
        super(message);
    }
}
