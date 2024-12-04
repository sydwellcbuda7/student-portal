package za.co.student_management.student_portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.student_management.student_portal.model.PasswordResetEntity;

import java.util.Optional;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordResetEntity, Long> {
    @Modifying
    @Query("UPDATE PasswordResetEntity pr SET pr.expiryDate = current_timestamp WHERE pr.id = " +
            "(SELECT p.id from PasswordResetEntity p JOIN p.user u WHERE u.id = :userId AND p.expiryDate > current_timestamp)")
    void markOldPasswordRestForUserAsExpired(@Param("userId") Long userId);

    Optional<PasswordResetEntity> findPasswordResetEntityByToken(String token);
}
