package servlets.schoolmanagement.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "requests")
public class RegistrationRequest {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "statut", nullable = false)
    private boolean statut;



    @OneToOne // Relation un-à-un avec Requester
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToOne // Relation un-à-un avec Requester
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;




    public RegistrationRequest(Student student) {
        this.id = UUID.randomUUID().toString();
        this.student = student;
        this.statut = false;
    }

    public RegistrationRequest(Teacher teacher) {
        this.id = UUID.randomUUID().toString();
        this.teacher = teacher;
        this.statut = false;
    }


    public RegistrationRequest() {
        // Constructeur par défaut
    }

    // Getters et Setters
}
