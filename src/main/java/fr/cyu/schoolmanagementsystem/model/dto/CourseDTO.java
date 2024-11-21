package fr.cyu.schoolmanagementsystem.model.dto;

import fr.cyu.schoolmanagementsystem.model.entity.Assignment;
import fr.cyu.schoolmanagementsystem.model.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.model.entity.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter @Setter
public class CourseDTO {

    private UUID id;


    private String name;

    private String description;


    private Teacher teacher;


    private Set<Enrollment> enrollments;


    private Set<Assignment> assignments;


}
