package fr.cyu.schoolmanagementsystem.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class CourseDTO {

    private UUID id;

    @NotBlank
    @Size(min = 2, max = 20)
    private String name;

    @Size(max = 255)
    private String description;

    private TeacherDTO teacher;

}
