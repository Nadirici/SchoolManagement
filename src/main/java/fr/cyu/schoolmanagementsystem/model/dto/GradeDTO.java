package fr.cyu.schoolmanagementsystem.model.dto;

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
    private double score;

}
