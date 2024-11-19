package fr.cyu.schoolmanagementsystem.model.dto;

import fr.cyu.schoolmanagementsystem.model.entity.Enrollment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter @Setter
public class StudentDTO extends PersonEntityDTO {

    private LocalDate dateOfBirth;


    private String firstname;


    private String lastname;


    private String email;

    private String salt;

    private String password;

    private boolean isVerified;

    private Set<Enrollment> enrollments;


}
