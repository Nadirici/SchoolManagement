package fr.cyu.schoolmanagementsystem.model.entity;

import fr.cyu.schoolmanagementsystem.model.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter @Setter
@Table(name = "grades")
public class Grade extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment;

    @Column(name = "score")
    private Double score;

}
