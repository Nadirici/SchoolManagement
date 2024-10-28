package services;

import DAO.StudentDAO;
import models.Student;

public class StudentService {
    private final StudentDAO studentDAO;

    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public void registerStudent(Student student) {
        // Logique supplémentaire si nécessaire
        studentDAO.save(student);
    }
}
