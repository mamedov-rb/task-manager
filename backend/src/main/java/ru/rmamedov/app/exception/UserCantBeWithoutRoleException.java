package ru.rmamedov.app.exception;

/**
 * @author Rustam Mamedov
 */

public class UserCantBeWithoutRoleException extends RuntimeException {

    public UserCantBeWithoutRoleException(String message) {
        super(message);
    }
}
