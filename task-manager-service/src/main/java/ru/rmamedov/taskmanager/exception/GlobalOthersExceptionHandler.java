package ru.rmamedov.taskmanager.exception;

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
import ru.rmamedov.taskmanager.exception.response.ResponseModel;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@ControllerAdvice
public class GlobalOthersExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public Map<String, String> handleBeenValidationExc(final MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        errors.put("time", LocalDateTime.now().toString());
        return errors;
    }

    @ExceptionHandler
    public ResponseEntity<ResponseModel> handleProjectNotFound(final Exception ex) {
        return new ResponseEntity<>(
                new ResponseModel(
                        ex.getMessage(),
                        LocalDateTime.now().toString()
                ),
                HttpStatus.BAD_REQUEST);
    }

}
