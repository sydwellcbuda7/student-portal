package za.co.student_management.student_portal.exception.custome;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccountNotVerifiedException extends RuntimeException {
    public AccountNotVerifiedException(){
        super("Email is not verified");
    }
}
