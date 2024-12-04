package za.co.student_management.student_portal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "password_reset")
public class PasswordResetEntity extends BaseAuditEntity {
    @Column(name = "token")
    private String token;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id")
    private UserEntity user;
}
