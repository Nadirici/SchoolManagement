package fr.cyu.schoolmanagementsystem.entity;

import fr.cyu.schoolmanagementsystem.entity.base.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "requests")
public class RegistrationRequest extends BaseEntity {

    @Column(nullable = false)
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}