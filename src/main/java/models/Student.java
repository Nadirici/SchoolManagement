package models;

import jakarta.persistence.*;
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

    public Student() {
        super();
    }

}