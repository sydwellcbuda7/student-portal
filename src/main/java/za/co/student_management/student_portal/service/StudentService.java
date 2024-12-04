package za.co.student_management.student_portal.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.co.student_management.student_portal.model.StudentEntity;

import java.util.List;

public interface StudentService {
    StudentEntity registerOrUpdateStudent(StudentEntity student);

    Page<StudentEntity> getAllStudents(StudentSpecification specification, Pageable pageable);

    StudentEntity getStudentById(Long id);

    void deleteStudent(Long id);
}
