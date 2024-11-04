package servlets.schoolmanagement.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import servlets.schoolmanagement.models.Enumerations.Departement;
import servlets.schoolmanagement.models.base.Person;

import java.sql.Date;
import java.util.UUID;


@Setter
@Getter
@Entity
@Table(name = "teachers") // Nom de la table dans la base de données
public class Teacher extends Person {

    @Id

    @Column(name = "teacher_id", nullable = false)
    private String id;


    @Column(name = "department")
    private String department;

    // Constructeur par défaut
    public Teacher() {
    }

    // Constructeur avec paramètres
    public Teacher( String firstName, String lastName, String email, String password,Departement department,String salt) {

        super(firstName,lastName,email,password,false,salt);
        this.department = department.toString();
        this.id = "1" + UUID.randomUUID();
    }




}
