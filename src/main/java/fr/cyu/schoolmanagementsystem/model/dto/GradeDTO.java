package fr.cyu.schoolmanagementsystem.model.dto;

import fr.cyu.schoolmanagementsystem.model.entity.Grade;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class GradeDTO {

    private UUID id;

    @NotNull
    private UUID enrollmentId;

    @NotNull
    private UUID assignmentId;

    @NotNull
    private Double score;

    private GradeDTO mapToGradeDTO(Grade grade) {
        GradeDTO gradeDTO = new GradeDTO();
        gradeDTO.setId(grade.getId());
        gradeDTO.setScore(grade.getScore());
        gradeDTO.setAssignmentId(grade.getAssignment().getId());
        gradeDTO.setEnrollmentId(grade.getEnrollment().getId());
        return gradeDTO;
    }


}
