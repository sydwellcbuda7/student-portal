package za.co.student_management.student_portal.exception.handler;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import za.co.student_management.student_portal.exception.ErrorResponse;
import za.co.student_management.student_portal.exception.custome.*;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException ex) {
        ErrorResponse errorResponse = new ErrorResponse("400", ex.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity<List<String>> handleValidationException(ValidationException exception,
                                                                         WebRequest request) {
        List<String> errors = new ArrayList<>();

        if (exception.getMessage() != null) {
            errors.add(exception.getMessage());
        } else {
            errors.add("Unknown validation error occurred");
        }

//        ErrorResponse error = new ErrorResponse("Validation failed", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }


    @ExceptionHandler(InvalidCredentialsException.class)
    public final ResponseEntity<Object> handleUserAuthentication(InvalidCredentialsException exception,
                                                                 WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(exception.getLocalizedMessage());
//        ErrorResponse error = new ErrorResponse("Invalid credentials provided", details);
        return new ResponseEntity<>(details, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("404", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("401", ex.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("403", ex.getLocalizedMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
//        ErrorResponse errorResponse = new ErrorResponse("500", ex.getLocalizedMessage());
//        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
