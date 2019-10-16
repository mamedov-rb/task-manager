package ru.rmamedov.taskmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rmamedov.taskmanager.exception.response.ResponseModel;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalUserExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleUserNotFound(final UserNotFoundException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        LocalDateTime.now().toString()
                ),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleUserNotAuthorized(final UserNotAuthorizedException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        LocalDateTime.now().toString()
                ),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleUserCantBeWithoutRole(final UserCantBeWithoutRoleException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        LocalDateTime.now().toString()
                ),
                HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleUserAlreadyExistsException(final UserAlreadyExistsException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        LocalDateTime.now().toString()
                ),
                HttpStatus.CONFLICT);
    }
}
