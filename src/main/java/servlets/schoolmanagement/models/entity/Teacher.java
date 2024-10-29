package servlets.schoolmanagement.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import servlets.schoolmanagement.models.base.Person;



@Setter
@Getter
@Entity
@Table(name = "teachers") // Nom de la table dans la base de données
public class Teacher extends Person {

    @Column(name = "department")
    private String department;

    // Constructeur par défaut
    public Teacher() {
    }

    // Constructeur avec paramètres
    public Teacher( String firstName, String lastName, String email, String password,String department) {

        super(firstName,lastName,email,password,false);
        this.department = department;
    }

}
