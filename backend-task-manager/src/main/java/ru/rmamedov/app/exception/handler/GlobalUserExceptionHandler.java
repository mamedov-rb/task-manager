package ru.rmamedov.app.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rmamedov.app.exception.UserAlreadyExistsException;
import ru.rmamedov.app.exception.UserCantBeWithoutRoleException;
import ru.rmamedov.app.exception.UserNotFoundException;

@ControllerAdvice
public class GlobalUserExceptionHandler {

    @ExceptionHandler
    public ResponseEntity handleUserNotFound(final UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleUserCantBeWithoutRole(final UserCantBeWithoutRoleException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleUserAlreadyExistsException(final UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }
}
