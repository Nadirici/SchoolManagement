package servlets.schoolmanagement.services;

import lombok.Data;
import servlets.schoolmanagement.models.entity.Student;
import org.springframework.stereotype.Service;
import servlets.schoolmanagement.repository.StudentRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@Data
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public void registerStudent(Student student) {

        studentRepository.save(student);
    }

    // Méthode pour vérifier si l'utilisateur existe déjà par email, nom et prénom
    public boolean userExists(String email, String lastname, String firstname) {
        return studentRepository.existsByEmailAndLastNameAndFirstName(email, lastname, firstname) ||
                studentRepository.existsByEmail(email);
    }

    // Méthode pour récupérer un étudiant par ID
    public Optional<Student> getById(String studentId) {
        return studentRepository.findById(studentId); // Utilise le méthode de recherche par ID de votre repository
    }


}
