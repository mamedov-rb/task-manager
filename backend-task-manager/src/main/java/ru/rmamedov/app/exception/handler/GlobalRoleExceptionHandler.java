package ru.rmamedov.app.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rmamedov.app.exception.RoleAlreadyExistsException;
import ru.rmamedov.app.exception.RoleNotFoundException;

@ControllerAdvice
public class GlobalRoleExceptionHandler {

    @ExceptionHandler
    public ResponseEntity handleRoleNotFound(final RoleNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }


    @ExceptionHandler
    public ResponseEntity handleRoleAlreadyExistsException(final RoleAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }
}
