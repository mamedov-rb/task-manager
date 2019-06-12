package ru.rmamedov.app.exception;

/**
 * @author Rustam Mamedov
 */

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
