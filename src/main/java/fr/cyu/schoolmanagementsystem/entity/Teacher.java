package fr.cyu.schoolmanagementsystem.entity;

import fr.cyu.schoolmanagementsystem.entity.base.BasePersonEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teachers")
public class Teacher extends BasePersonEntity {

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Course> courses;

    @Column(name = "department")
    private String department;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RegistrationRequest> registrationRequests;

    public Teacher() {
        super();
        this.courses = new HashSet<>();
    }

    public Teacher(String firstname, String lastname, String email, String password, String department, String salt, boolean isVerified) {
        super(firstname, lastname, email, password, salt, isVerified);
        this.department = department;
    }

    public Teacher(String firstName, String lastName, String email, String hashedPassword, String salt) {
        super(firstName, lastName, email, hashedPassword, salt, false);
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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
