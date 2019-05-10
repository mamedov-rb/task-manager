package ru.rmamedov.app.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rmamedov.app.exception.ProjectAlreadyExistsException;
import ru.rmamedov.app.exception.ProjectNotFoundException;

@ControllerAdvice
public class GlobalProjectExceptionHandler {

    @ExceptionHandler
    public ResponseEntity handleProjectNotFound(final ProjectNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleProjectAlreadyExistsException(final ProjectAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }
}
