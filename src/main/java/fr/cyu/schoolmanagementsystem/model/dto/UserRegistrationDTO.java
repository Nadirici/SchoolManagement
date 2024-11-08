package fr.cyu.schoolmanagementsystem.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRegistrationDTO {
    private String userType;
    private String email;
    private String lastname;
    private String firstname;
    private LocalDate dateOfBirth;
    private String password;
    private String department;
}
