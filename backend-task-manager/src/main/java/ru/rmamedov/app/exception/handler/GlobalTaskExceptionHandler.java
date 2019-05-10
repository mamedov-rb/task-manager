package ru.rmamedov.app.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rmamedov.app.exception.TaskAlreadyExistsException;
import ru.rmamedov.app.exception.TaskNotFoundException;

@ControllerAdvice
public class GlobalTaskExceptionHandler {

    @ExceptionHandler
    public ResponseEntity handleTaskNotFound(final TaskNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleTaskAlreadyExistsException(final TaskAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }
}
