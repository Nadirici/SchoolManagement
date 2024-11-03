package fr.cyu.schoolmanagementsystem.model.entity;

import fr.cyu.schoolmanagementsystem.model.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "grades")
public class Grade extends BaseEntity {

    @Column(name = "value", nullable = false)
    private double value;

    @ManyToOne
    private Enrollment enrollment;

}
