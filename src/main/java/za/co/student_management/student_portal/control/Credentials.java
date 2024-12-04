package za.co.student_management.student_portal.control;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Credentials {
    private String username;

    private String password;
}
