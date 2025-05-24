package za.co.student_management.student_portal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserEntity extends BaseAuditEntity {

    @NotNull
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "deleted")
    private boolean deleted;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserRoleEntity> assignedRoles = new HashSet<>();

}
