package fr.cyu.schoolmanagementsystem.model.entity;

import fr.cyu.schoolmanagementsystem.model.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Getter
@Table(name = "courses")
public class Course extends BaseEntity {

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Column(name = "description")
    private String description;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private Set<Enrollment> enrollments;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private Set<Assignment> assignments;

    public Course() {
        enrollments = new HashSet<>();
        assignments = new HashSet<>();
    }

    public Course(String name, String description, Teacher teacher) {
        this.name = name;
        this.description = description;
        this.teacher = teacher;
    }

    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public void removeAssignment(Assignment assignment) {
        assignments.remove(assignment);
    }

}
