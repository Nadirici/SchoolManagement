package servlets.schoolmanagement.models.entity;

import jakarta.persistence.*;
import servlets.schoolmanagement.models.base.Person;

import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "students")

public class Student extends Person {

    @Column(name = "date_of_birth")
    private Date dateOfBirth;



    public Student(String studentFirstName, String studentLastName, Date studentBirthDate, String studentEmail, String studentPassword) {
        super(studentFirstName, studentLastName, studentEmail, studentPassword, false,"2" + UUID.randomUUID());
        this.dateOfBirth = studentBirthDate;


    }



    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Student() {
        super();
    }

}