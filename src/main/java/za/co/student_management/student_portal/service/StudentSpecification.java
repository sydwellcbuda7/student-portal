package za.co.student_management.student_portal.service;


import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import za.co.student_management.student_portal.model.StudentEntity;
import za.co.student_management.student_portal.model.UserRoleEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentSpecification {
    private String email;
    private Boolean delete;
    private String firstName;
    private String lastName;
    private String studentNumber;
    private String gender;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Set<UserRoleEntity> roles;

    public Specification<StudentEntity> createSpecification() {
        List<Predicate> predicates = new ArrayList<>();
        return (root, criteriaQuery, cb) -> {
            if (Objects.nonNull(email)) {
                predicates.add(withEmail().toPredicate(root, criteriaQuery, cb));
            }
            if (Objects.nonNull(delete)) {
                predicates.add(withDelete().toPredicate(root, criteriaQuery, cb));
            }
         
            if (Objects.nonNull(firstName)) {
                predicates.add(withFirstName().toPredicate(root, criteriaQuery, cb));
            }
            if (Objects.nonNull(lastName)) {
                predicates.add(withLastName().toPredicate(root, criteriaQuery, cb));
            }
            if (Objects.nonNull(studentNumber)) {
                predicates.add(withStudentNumber().toPredicate(root, criteriaQuery, cb));
            }
            if (Objects.nonNull(gender)) {
                predicates.add(withGender().toPredicate(root, criteriaQuery, cb));
            }
            if (Objects.nonNull(dateFrom)) {
                predicates.add(withDateFrom().toPredicate(root, criteriaQuery, cb));
            }
            if (Objects.nonNull(dateTo)) {
                predicates.add(withDateTo().toPredicate(root, criteriaQuery, cb));
            }
            if (Objects.nonNull(roles)) {
                predicates.add(withRoles().toPredicate(root, criteriaQuery, cb));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Specification<StudentEntity> withEmail() {
        return (root, query, cb) -> cb.equal(root.get("email"), email);
    }

    private Specification<StudentEntity> withDelete() {
        return (root, query, cb) -> cb.equal(root.get("delete"), delete);
    }

    private Specification<StudentEntity> withFirstName() {
        return (root, query, cb) -> cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    private Specification<StudentEntity> withLastName() {
        return (root, query, cb) -> cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    private Specification<StudentEntity> withStudentNumber() {
        return (root, query, cb) -> cb.like(root.get("studentNumber"), studentNumber);
    }

    private Specification<StudentEntity> withGender() {
        return (root, query, cb) -> cb.equal(root.get("gender"), gender);
    }

    private Specification<StudentEntity> withDateFrom() {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("creationTimestamp"), dateFrom);
    }

    private Specification<StudentEntity> withDateTo() {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("creationTimestamp"), dateTo);
    }

    private Specification<StudentEntity> withRoles() {
        return (root, query, cb) -> {
            Join<StudentEntity, UserRoleEntity> roleJoin = root.join("assignedRoles");
            return roleJoin.get("roleName").in(roles);
        };
    }
}
