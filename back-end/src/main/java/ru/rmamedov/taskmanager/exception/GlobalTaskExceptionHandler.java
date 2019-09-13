package ru.rmamedov.taskmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rmamedov.taskmanager.exception.response.ResponseModel;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalTaskExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleTaskNotFound(final TaskNotFoundException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        LocalDateTime.now().toString()
                ),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleTaskAlreadyExistsException(final TaskAlreadyExistsException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        LocalDateTime.now().toString()
                ),
                HttpStatus.CONFLICT);
    }
}
