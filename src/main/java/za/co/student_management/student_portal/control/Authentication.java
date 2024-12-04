package za.co.student_management.student_portal.control;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.student_management.student_portal.model.datatype.Role;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Authentication {
    private String token;
    private Long userId;
    private Set<Role> roles;
}
