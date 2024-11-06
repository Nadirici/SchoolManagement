package fr.cyu.schoolmanagementsystem.controller;

import fr.cyu.schoolmanagementsystem.model.dto.GradeDTO;
import fr.cyu.schoolmanagementsystem.service.GradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    public final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> gradeStudentToCourse(@RequestBody GradeDTO gradeDTO) {
        UUID gradeId = gradeService.gradeStudent(gradeDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Student graduate successfully");
        response.put("id", gradeId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable UUID id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}
