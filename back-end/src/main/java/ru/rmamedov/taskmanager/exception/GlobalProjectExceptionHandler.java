package ru.rmamedov.taskmanager.exception;

/**
 * @author Rustam Mamedov
 */

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import ru.rmamedov.taskmanager.exception.response.ResponseModel;

import java.time.LocalDateTime;

@RestController
@ControllerAdvice
public class GlobalProjectExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleProjectNotFound(final ProjectNotFoundException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        LocalDateTime.now().toString()
                ),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleProjectAlreadyExistsException(final ProjectAlreadyExistsException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        LocalDateTime.now().toString()
                ),
                HttpStatus.CONFLICT);
    }
}
