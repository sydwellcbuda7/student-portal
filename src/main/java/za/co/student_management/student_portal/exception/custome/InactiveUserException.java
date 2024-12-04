package za.co.student_management.student_portal.exception.custome;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED)
public class InactiveUserException extends RuntimeException {
    public InactiveUserException() {
        super("User account has been deactivated. Please contact administrator for support.");
    }
}
