package fr.cyu.schoolmanagementsystem.controller;

import fr.cyu.schoolmanagementsystem.model.dto.CourseDTO;
import fr.cyu.schoolmanagementsystem.model.dto.EnrollmentDTO;
import fr.cyu.schoolmanagementsystem.model.dto.StudentDTO;
import fr.cyu.schoolmanagementsystem.model.entity.Course;
import fr.cyu.schoolmanagementsystem.model.entity.Student;
import fr.cyu.schoolmanagementsystem.service.CourseService;
import fr.cyu.schoolmanagementsystem.service.EnrollmentService;
import fr.cyu.schoolmanagementsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;


    private final StudentService studentService;
    private final CourseService courseService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService,StudentService studentService,CourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
        List<EnrollmentDTO> enrollments = enrollmentService.getAllEnrollments();
        return new ResponseEntity<>(enrollments, HttpStatus.OK);
    }






    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable UUID id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}
