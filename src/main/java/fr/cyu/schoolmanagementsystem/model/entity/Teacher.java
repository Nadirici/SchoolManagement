package fr.cyu.schoolmanagementsystem.model.entity;

import fr.cyu.schoolmanagementsystem.model.dto.TeacherDTO;
import fr.cyu.schoolmanagementsystem.model.entity.base.BasePersonEntity;
import fr.cyu.schoolmanagementsystem.model.entity.enumeration.Departement;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name = "teachers")
public class Teacher extends BasePersonEntity {

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
    private Set<Course> courses;

    @Setter
    @Column(name = "department")
    private String department;

    public Teacher() {
        super();
        this.courses = new HashSet<>();
    }

    public Teacher(String firstname, String lastname, String email, String password, Departement departement, String salt) {
        super(firstname, lastname, email, password, salt);
        this.department = departement.toString();
    }

    public Teacher(TeacherDTO teacherDTO) {
        super(teacherDTO.getFirstname(),teacherDTO.getLastname(),teacherDTO.getEmail(),teacherDTO.getPassword(),teacherDTO.getSalt());
        this.department = teacherDTO.getDepartment();
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
    }

}
