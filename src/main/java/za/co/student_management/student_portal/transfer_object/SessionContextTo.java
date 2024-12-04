package za.co.student_management.student_portal.transfer_object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.student_management.student_portal.model.datatype.Role;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionContextTo {
    private Long userId;
    private String studentNumber;
    private String name;
    private String email;
    private Role role;
}
