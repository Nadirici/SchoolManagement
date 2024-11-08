package fr.cyu.schoolmanagementsystem.model.entity;

import fr.cyu.schoolmanagementsystem.model.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "requests")
public class RegistrationRequest extends BaseEntity {

    @Column(name = "status", nullable = false)
    private boolean status;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public RegistrationRequest(Student student) {
        this.student = student;
        this.status = false;
    }

    public RegistrationRequest(Teacher teacher) {
        this.teacher = teacher;
        this.status = false;
    }

    public RegistrationRequest() {
        super();
    }

}