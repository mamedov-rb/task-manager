package ru.rmamedov.app.controller.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rmamedov.app.controller.exceptionHandler.responseModel.ResponseModel;
import ru.rmamedov.app.exception.TaskAlreadyExistsException;
import ru.rmamedov.app.exception.TaskNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalTaskExceptionHandler {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss");

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleTaskNotFound(final TaskNotFoundException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        dtf.format(LocalDateTime.now())
                ),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleTaskAlreadyExistsException(final TaskAlreadyExistsException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        dtf.format(LocalDateTime.now())
                ),
                HttpStatus.CONFLICT);
    }
}
