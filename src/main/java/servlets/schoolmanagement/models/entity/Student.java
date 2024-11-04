package servlets.schoolmanagement.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import org.springframework.lang.NonNull;
import servlets.schoolmanagement.models.base.Person;

import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "students")
@Getter
@Setter

public class Student extends Person {


    @Id
    @NonNull
    @Column(name = "student_id")
    private String id;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;




    public Student(String studentFirstName, String studentLastName, LocalDate studentBirthDate, String studentEmail, String studentPassword, String salt) {
        super(studentFirstName, studentLastName, studentEmail, studentPassword, false,salt);
        this.dateOfBirth = studentBirthDate;
        this.id = "2" + UUID.randomUUID().toString();

    }





    public Student() {
        super();
    }

}