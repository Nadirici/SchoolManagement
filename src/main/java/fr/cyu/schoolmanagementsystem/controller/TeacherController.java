package fr.cyu.schoolmanagementsystem.controller;

import fr.cyu.schoolmanagementsystem.model.dto.TeacherDTO;
import fr.cyu.schoolmanagementsystem.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        return ResponseEntity
                .ok(teacherService.getAllTeachers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable("id") UUID teacherId) {
        Optional<TeacherDTO> teacher = this.teacherService.getTeacherById(teacherId);

        return teacher.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity
                .notFound()
                .build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addTeacher(@Valid @RequestBody TeacherDTO teacherDTO,
                                            UriComponentsBuilder uriComponentsBuilder) {
        try {

            UUID newTeacherId = teacherService.addTeacher(teacherDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Teacher created successfully");
            response.put("id", newTeacherId);

            return ResponseEntity
                    .created(uriComponentsBuilder.path("/teachers/{id}")
                            .build(newTeacherId))
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
    public ResponseEntity<Map<String, Object>> deleteTeacherById(@PathVariable("id") UUID id) {
        try {

            teacherService.deleteTeacherById(id);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Teacher deleted successfully");

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
}
