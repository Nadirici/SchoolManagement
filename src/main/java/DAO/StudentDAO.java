package DAO;

import models.Student;
import org.springframework.stereotype.Repository;

@Repository
public class StudentDAO extends GenericDAOImpl<Student> {
    public StudentDAO() {

        super(Student.class);
    }
}
