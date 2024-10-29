package servlets.schoolmanagement.models.entity;

import jakarta.persistence.*;
import servlets.schoolmanagement.models.base.Person;

import java.sql.Date;

@Entity
@Table(name = "students")

public class Student extends Person {

    @Column(name = "date_of_birth")
    private Date dateOfBirth;



    public Student(String studentFirstName, String studentLastName, Date studentBirthDate, String studentEmail, String studentPassword) {
        super(studentFirstName, studentLastName, studentEmail, studentPassword, false);
        this.dateOfBirth = studentBirthDate;


    }
    public Student(String studentFirstName, String studentLastName, Date studentBirthDate, String studentEmail, String studentPassword, String id) {
        super(studentFirstName, studentLastName, studentEmail, studentPassword, false);
        this.dateOfBirth = studentBirthDate;
        super.setId(id); // Utilisation de l'ID passé en paramètre
    }

    public Student() {
        super();
    }

}