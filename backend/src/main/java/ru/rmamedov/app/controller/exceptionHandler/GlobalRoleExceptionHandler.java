package ru.rmamedov.app.controller.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rmamedov.app.controller.exceptionHandler.responseModel.ResponseModel;
import ru.rmamedov.app.exception.RoleAlreadyExistsException;
import ru.rmamedov.app.exception.RoleNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalRoleExceptionHandler {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss");

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleRoleNotFound(final RoleNotFoundException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        dtf.format(LocalDateTime.now())
                ),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleRoleAlreadyExistsException(final RoleAlreadyExistsException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        dtf.format(LocalDateTime.now())
                ),
                HttpStatus.CONFLICT);
    }
}
