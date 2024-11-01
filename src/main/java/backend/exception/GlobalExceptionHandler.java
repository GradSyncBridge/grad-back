package backend.exception;

import backend.exception.model.BaseException;
import backend.util.ResultEntity;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity exceptionHandler(BaseException ex){
        System.out.println(ex.getMessage());
        return ResultEntity.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity exceptionHandler(SQLIntegrityConstraintViolationException ex){
        return ResultEntity.error(ex.getMessage());
    }

   @ExceptionHandler
    public ResponseEntity<ResultEntity<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = "Validation error: " + ex.getMessage();
        int startIndex = message.lastIndexOf("message [") + "message [".length();
        int endIndex = message.lastIndexOf("]") - 1;

        return ResultEntity.error(HttpStatus.BAD_REQUEST.value(), message.substring(startIndex, endIndex).trim());
    }

    @ExceptionHandler
    public ResponseEntity<ResultEntity<String>> handleConstraintViolation(ConstraintViolationException ex) {
        StringBuilder message = new StringBuilder("Constraint violation: ");
        ex.getConstraintViolations().forEach(violation -> {
            message.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("; ");
        });
        return ResultEntity.error(HttpStatus.BAD_REQUEST.value(), message.toString());
    }

    @ExceptionHandler
    public ResponseEntity<ResultEntity<String>> handleClassCastException(ClassCastException ex) {
        return ResultEntity.error(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

}