package fr.cyu.schoolmanagementsystem.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class AssignmentDTO {

    private UUID id;

    @NotBlank
    @Size(min = 2, max = 20)
    private String title;

    @Size(max = 255)
    private String description;

    private double coefficient;

    private CourseDTO course;

}
