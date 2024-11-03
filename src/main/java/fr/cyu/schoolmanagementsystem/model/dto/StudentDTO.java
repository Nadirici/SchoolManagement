package fr.cyu.schoolmanagementsystem.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class StudentDTO extends PersonEntityDTO {

    private LocalDate dateOfBirth;

}
