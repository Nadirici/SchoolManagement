package fr.cyu.schoolmanagementsystem.controller;

import fr.cyu.schoolmanagementsystem.model.dto.CourseDTO;
import fr.cyu.schoolmanagementsystem.model.dto.StudentDTO;
import fr.cyu.schoolmanagementsystem.service.EnrollmentService;
import fr.cyu.schoolmanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    private final EnrollmentService enrollmentService;

    @Autowired
    public StudentController(StudentService studentService, EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return ResponseEntity
                .ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable("id") UUID studentId) {
        Optional<StudentDTO> student = this.studentService.getStudentById(studentId);

        return student.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity
                .notFound()
                .build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addStudent(@Valid @RequestBody StudentDTO studentDTO,
                                            UriComponentsBuilder uriComponentsBuilder) {
        try {

            UUID newStudentId = studentService.addStudent(studentDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Student created successfully");
            response.put("id", newStudentId);

            return ResponseEntity
                    .created(uriComponentsBuilder.path("/students/{id}")
                            .build(newStudentId))
                    .body(response);

        } catch (RuntimeException ex) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Conflict");
            errorResponse.put("message", ex.getMessage());

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteStudentById(@PathVariable("id") UUID id) {
        try {

            studentService.deleteStudentById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Student deleted successfully");

            return ResponseEntity.ok(response);

        } catch(RuntimeException ex) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Conflict");
            errorResponse.put("message", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(errorResponse);
        }
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<List<CourseDTO>> getCoursesForStudent(@PathVariable UUID id) {
        List<CourseDTO> courses = enrollmentService.getCoursesForStudent(id);
        return ResponseEntity.ok(courses);
    }
}
