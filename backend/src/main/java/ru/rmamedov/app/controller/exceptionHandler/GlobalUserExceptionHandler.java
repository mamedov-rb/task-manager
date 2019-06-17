package ru.rmamedov.app.controller.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rmamedov.app.controller.exceptionHandler.responseModel.ResponseModel;
import ru.rmamedov.app.exception.UserAlreadyExistsException;
import ru.rmamedov.app.exception.UserCantBeWithoutRoleException;
import ru.rmamedov.app.exception.UserNotAuthorizedException;
import ru.rmamedov.app.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalUserExceptionHandler {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss");

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleUserNotFound(final UserNotFoundException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        dtf.format(LocalDateTime.now())
                ),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleUserNotAuthorized(final UserNotAuthorizedException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        dtf.format(LocalDateTime.now())
                ),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleUserCantBeWithoutRole(final UserCantBeWithoutRoleException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        dtf.format(LocalDateTime.now())
                ),
                HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleUserAlreadyExistsException(final UserAlreadyExistsException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        dtf.format(LocalDateTime.now())
                ),
                HttpStatus.CONFLICT);
    }
}
