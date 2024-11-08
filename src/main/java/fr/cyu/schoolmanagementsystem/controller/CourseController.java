package fr.cyu.schoolmanagementsystem.controller;

import fr.cyu.schoolmanagementsystem.model.dto.CourseDTO;
import fr.cyu.schoolmanagementsystem.model.dto.StudentDTO;
import fr.cyu.schoolmanagementsystem.service.CourseService;
import fr.cyu.schoolmanagementsystem.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    private final EnrollmentService enrollmentService;

    @Autowired
    public CourseController(CourseService courseService, EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity
                .ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable("id") UUID courseId) {
        Optional<CourseDTO> course = this.courseService.getCourseById(courseId);

        return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity
                .notFound()
                .build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addCourse(@Valid @RequestBody CourseDTO courseDTO,
                                                          UriComponentsBuilder uriComponentsBuilder) {
        try {

            UUID newCourseId = courseService.addCourse(courseDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Course created successfully");
            response.put("id", newCourseId);

            return ResponseEntity
                    .created(uriComponentsBuilder.path("/courses/{id}")
                            .build(newCourseId))
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
    public ResponseEntity<Map<String, Object>> deleteCourseById(@PathVariable("id") UUID id) {
        try {

            courseService.deleteCourse(id);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Course deleted successfully");

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

    @GetMapping("/{id}/students")
    public ResponseEntity<List<StudentDTO>> getStudentsForCourse(@PathVariable UUID id) {
        List<StudentDTO> students = enrollmentService.getStudentsForCourse(id);
        return ResponseEntity.ok(students);
    }
}
