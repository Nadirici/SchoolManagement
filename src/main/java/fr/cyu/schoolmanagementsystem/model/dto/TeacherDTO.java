package fr.cyu.schoolmanagementsystem.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TeacherDTO extends PersonEntityDTO {
    private String department;
}
