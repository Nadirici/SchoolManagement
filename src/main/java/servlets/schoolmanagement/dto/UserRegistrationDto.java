package servlets.schoolmanagement.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String userType;
    private String email;
    private String lastname;
    private String firstname;
    private String dateOfBirth;
    private String password;
    private String department;
}
