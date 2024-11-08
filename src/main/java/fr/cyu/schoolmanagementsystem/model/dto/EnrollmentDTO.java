package fr.cyu.schoolmanagementsystem.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class EnrollmentDTO {

    private UUID id;

    @NotNull
    private UUID studentId;

    @NotNull
    private UUID courseId;

}
