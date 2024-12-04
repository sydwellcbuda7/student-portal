package za.co.student_management.student_portal.transfer_object;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChangePasswordTo {

    private String token;

    @NotEmpty
    private String password;
}
