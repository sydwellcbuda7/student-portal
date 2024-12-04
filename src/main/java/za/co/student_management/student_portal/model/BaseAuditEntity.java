package za.co.student_management.student_portal.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.time.LocalDateTime;

/**
 * Base class for each entity which need audit data, like.
 */
@Getter @Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseAuditEntity extends BaseEntity {

    @Column(name = "creation_timestamp")
    private LocalDateTime creationTimestamp;

    @Column(name = "modification_timestamp")
    private LocalDateTime modificationTimestamp;

    @Override
    protected void setCreationParameters() {
        super.setCreationParameters();
        creationTimestamp = LocalDateTime.now();
    }

    @PreUpdate
    private void modificationAt() {
        modificationTimestamp = LocalDateTime.now();
    }
}
