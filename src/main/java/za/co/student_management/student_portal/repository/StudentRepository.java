package za.co.student_management.student_portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.student_management.student_portal.model.StudentEntity;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long>, JpaSpecificationExecutor<StudentEntity> {

    @Modifying
    @Query("UPDATE StudentEntity t SET t.deleted = true WHERE t.id = :id")
    void deleteStudent(@Param("id") Long studentId);
}
