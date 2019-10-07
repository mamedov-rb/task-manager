package ru.rmamedov.taskmanager.exception;

/**
 * @author Rustam Mamedov
 */

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
