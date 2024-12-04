package za.co.student_management.student_portal.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.hibernate.ResourceClosedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.co.student_management.student_portal.exception.custome.InvalidInputException;
import za.co.student_management.student_portal.exception.custome.ResourceNotFoundException;
import za.co.student_management.student_portal.model.StudentEntity;
import za.co.student_management.student_portal.model.UserEntity;
import za.co.student_management.student_portal.model.UserRoleEntity;
import za.co.student_management.student_portal.model.datatype.Role;
import za.co.student_management.student_portal.repository.StudentRepository;
import za.co.student_management.student_portal.repository.UserRepository;
import za.co.student_management.student_portal.service.StudentService;
import za.co.student_management.student_portal.service.StudentSpecification;

import java.time.Year;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @Override
    public StudentEntity registerOrUpdateStudent(StudentEntity student) {
        if (student.getId() != null) {
            StudentEntity existingStudent = studentRepository.findById(student.getId())
                    .orElseThrow(() -> new ResourceClosedException("Student not found"));

            student.setVersion(existingStudent.getVersion());
            existingStudent.getAssignedRoles().clear();
            existingStudent.getAssignedRoles().addAll(student.getAssignedRoles());
        }

        if (student.getId() == null) {
            Optional<UserEntity> user = userRepository.findUserEntityByEmail(student.getEmail());
            if (user.isPresent()) {
                throw new InvalidInputException("Email already registered");
            }
            student.getAssignedRoles().clear();
            student.getAssignedRoles().add(new UserRoleEntity(Role.STUDENT));
            student.setStudentNumber(generateStudentNumber());
            student.setPassword(passwordEncoder.encode(student.getPassword()));

        }

        return studentRepository.save(student);
    }


    @Override
    public Page<StudentEntity> getAllStudents(StudentSpecification specification, Pageable pageable) {
        Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("creationTimestamp").descending());
        return studentRepository.findAll(specification.createSpecification(), pageable1);
    }

    @Override
    public StudentEntity getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
    }

    @Override
    public void deleteStudent(Long id) {
    studentRepository.deleteStudent(id);
    }

    private String generateStudentNumber() {
        String year = String.valueOf(Year.now().getValue());
        year = year.charAt(0) + year.substring(2); // Removing the second digit
        return year +  UUID.randomUUID().toString().replaceAll("[^0-9]", "").substring(0, 6); // Ensures uniqueness

    }
}
