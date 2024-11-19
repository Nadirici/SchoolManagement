package fr.cyu.schoolmanagementsystem.model.entity;

import fr.cyu.schoolmanagementsystem.model.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "assignments")
@Getter @Setter
public class Assignment extends BaseEntity {

    @NotNull
    private String title;

    private String description;

    private double coefficient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "assignment")
    private Set<Grade> grades;

    public Assignment() {
        grades = new HashSet<>();
    }

    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    public void removeGrade(Grade grade) {
        grades.remove(grade);
    }
}
