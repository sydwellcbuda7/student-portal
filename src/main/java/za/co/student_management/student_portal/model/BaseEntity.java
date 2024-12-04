package za.co.student_management.student_portal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Objects;

/**
 * Base class for each entity.
 * It contains common attributes for all entities:
 */
@Getter @Setter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @PrePersist
    protected void setCreationParameters() {
        version = 0;
    }


    @Override
    public int hashCode() {
        return Objects.isNull(id) ? 0 : id.hashCode();
    }
}
