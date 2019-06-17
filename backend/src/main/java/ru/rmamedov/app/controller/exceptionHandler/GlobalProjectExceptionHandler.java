package ru.rmamedov.app.controller.exceptionHandler;

/**
 * @author Rustam Mamedov
 */

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import ru.rmamedov.app.controller.exceptionHandler.responseModel.ResponseModel;
import ru.rmamedov.app.exception.ProjectAlreadyExistsException;
import ru.rmamedov.app.exception.ProjectNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@ControllerAdvice
public class GlobalProjectExceptionHandler {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss");

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleProjectNotFound(final ProjectNotFoundException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        dtf.format(LocalDateTime.now())
                ),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleProjectAlreadyExistsException(final ProjectAlreadyExistsException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        dtf.format(LocalDateTime.now())
                ),
                HttpStatus.CONFLICT);
    }
}
