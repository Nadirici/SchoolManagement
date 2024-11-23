package fr.cyu.schoolmanagementsystem.entity;

import fr.cyu.schoolmanagementsystem.entity.base.BasePersonEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "students")
public class Student extends BasePersonEntity {

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Enrollment> enrollments;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RegistrationRequest> registrationRequests;

    public Student() {
        super();
        this.enrollments = new HashSet<>();
    }

    public Student(String firstname, String lastname, LocalDate dateOfBirth, String email, String password, String salt, boolean isVerified) {
        super(firstname, lastname, email, password, salt, isVerified);
        this.dateOfBirth = dateOfBirth;
    }

    public Student(String firstName, String lastName, String email, String hashedPassword, String salt) {
        this(firstName, lastName, LocalDate.now(), email, hashedPassword, salt, false);
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
    }

    public void removeEnrollment(Enrollment enrollment) {
        this.enrollments.remove(enrollment);
    }

    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void addRegistrationRequest(RegistrationRequest registrationRequest) {
        this.registrationRequests.add(registrationRequest);
    }

    public void removeRegistrationRequest(RegistrationRequest registrationRequest) {
        this.registrationRequests.remove(registrationRequest);
    }

    public Set<RegistrationRequest> getRegistrationRequests() {
        return registrationRequests;
    }
}
