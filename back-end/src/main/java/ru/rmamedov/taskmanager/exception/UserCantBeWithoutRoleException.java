package ru.rmamedov.taskmanager.exception;

/**
 * @author Rustam Mamedov
 */

public class UserCantBeWithoutRoleException extends RuntimeException {

    public UserCantBeWithoutRoleException(String message) {
        super(message);
    }
}
