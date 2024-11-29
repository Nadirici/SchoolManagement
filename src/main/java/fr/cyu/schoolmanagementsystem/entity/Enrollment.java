package fr.cyu.schoolmanagementsystem.entity;

import fr.cyu.schoolmanagementsystem.entity.base.BaseEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "enrollments")
public class Enrollment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false, referencedColumnName = "id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id",  nullable = false, referencedColumnName = "id")
    private Course course;

    @OneToMany(mappedBy = "enrollment", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Grade> grades;

    public Enrollment() {
        grades = new HashSet<>();
    }

    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    public void removeGrade(Grade grade) {
        grades.remove(grade);
    }

    public Set<Grade> getGrades() {
        return grades;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
