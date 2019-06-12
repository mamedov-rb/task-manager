package ru.rmamedov.app.controller.exceptionHandler;

/**
 * @author Rustam Mamedov
 */

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.rmamedov.app.controller.exceptionHandler.responseModel.ResponseModel;
import ru.rmamedov.app.exception.BadRequestException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@ControllerAdvice
public class GlobalOthersExceptionHandler {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss");

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public Map<String, String> handleConstraintViolation(final MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        errors.put("time", dtf.format(LocalDateTime.now()));
        return errors;
    }

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleBadRequest(final BadRequestException ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        dtf.format(LocalDateTime.now())
                ),
                HttpStatus.BAD_REQUEST);
    }
}
