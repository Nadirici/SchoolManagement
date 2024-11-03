package fr.cyu.schoolmanagementsystem.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class StudentDTO extends PersonEntityDTO {

    @Getter @Setter
    private LocalDate dateOfBirth;

}
