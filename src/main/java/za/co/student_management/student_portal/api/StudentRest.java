package za.co.student_management.student_portal.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.co.student_management.student_portal.model.StudentEntity;
import za.co.student_management.student_portal.model.UserRoleEntity;
import za.co.student_management.student_portal.service.StudentService;
import za.co.student_management.student_portal.service.StudentSpecification;

import java.time.LocalDateTime;
import java.util.Set;


@RestController
@RequestMapping(path = "/student")
@RequiredArgsConstructor
public class StudentRest {

    private final StudentService studentService;

    @Operation(
            summary = "Create a new student",
            description = "Create a new student using the provided details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Student successfully created",
                            content = @Content(schema = @Schema(implementation = StudentEntity.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid student data provided"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PermitAll
    public ResponseEntity<StudentEntity> registerOrUpdateStudent(@Valid @RequestBody StudentEntity student) {
        StudentEntity createdStudent = studentService.registerOrUpdateStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @Operation(
            summary = "Get all students",
            description = "Retrieve a list of all students",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Students retrieved successfully",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = StudentEntity.class)))),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PermitAll
    public ResponseEntity<Page<StudentEntity>> getAllStudents(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean delete,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String studentNumber,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo,
            @RequestParam(required = false) Set<UserRoleEntity> roles,
            Pageable pageable) {

        StudentSpecification specification = new StudentSpecification(email, delete, firstName, lastName,
                studentNumber, gender, dateFrom, dateTo, roles);

        Page<StudentEntity> students = studentService.getAllStudents(specification, pageable);
        return ResponseEntity.ok(students);
    }

    @Operation(
            summary = "Get a student by ID",
            description = "Retrieve details of a student using their ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Student retrieved successfully",
                            content = @Content(schema = @Schema(implementation = StudentEntity.class))),
                    @ApiResponse(responseCode = "404", description = "Student not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PermitAll
    public ResponseEntity<StudentEntity> getStudentById(@PathVariable Long id) {
        StudentEntity student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }


    @Operation(
            summary = "Delete a student by ID",
            description = "Delete a student using their ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Student not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @DeleteMapping(value = "/{id}")
    @PermitAll
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
