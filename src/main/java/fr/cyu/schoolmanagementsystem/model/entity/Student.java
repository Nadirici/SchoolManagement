package fr.cyu.schoolmanagementsystem.model.entity;

import fr.cyu.schoolmanagementsystem.model.entity.base.BasePersonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Table(name = "students")
public class Student extends BasePersonEntity {

    @Setter
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<Enrollment> enrollments;

    public Student() {
        super();
        enrollments = new HashSet<>();
    }

    public Student(String firstname, String lastname, LocalDate dateOfBirth, String email, String password, String salt) {
        super(firstname, lastname, email, password, salt);
        this.dateOfBirth = dateOfBirth;

    }

    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
    }

}
