package za.co.student_management.student_portal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.student_management.student_portal.model.datatype.Role;


@Getter
@Setter
@Entity
@Table(name = "user_role")
@NoArgsConstructor
public class UserRoleEntity extends BaseEntity {
    @Column(name = "name")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    public UserRoleEntity(Role role) {
        this.role = role;
    }
}
