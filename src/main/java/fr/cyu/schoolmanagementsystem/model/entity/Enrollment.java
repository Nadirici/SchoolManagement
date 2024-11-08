package fr.cyu.schoolmanagementsystem.model.entity;

import fr.cyu.schoolmanagementsystem.model.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name = "enrollments")
public class Enrollment extends BaseEntity {

    @Setter
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false, referencedColumnName = "id")
    private Student student;

    @Setter
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false, referencedColumnName = "id")
    private Course course;

    @OneToMany(mappedBy = "enrollment")
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

}
