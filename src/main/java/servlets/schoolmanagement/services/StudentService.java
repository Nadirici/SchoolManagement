package servlets.schoolmanagement.services;


import lombok.Data;
import servlets.schoolmanagement.models.Student;
import org.springframework.stereotype.Service;
import servlets.schoolmanagement.repository.StudentRepository;


import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public void registerStudent(Student student) {
        student.setId(generateUniqueId());
        studentRepository.save(student);
    }
    // Méthode pour vérifier si l'utilisateur existe déjà par email, nom et prénom
    public boolean userExists(String email, String lastname, String firstname) {
        return studentRepository.existsByEmailAndLastNameAndFirstName(email, lastname, firstname) ||  studentRepository.existsByEmail(email);
    }
    // Génère un ID unique qui commence par "2" et vérifie qu'il n'existe pas déjà en base
    private String generateUniqueId() {
        String id;
        do {
            id = "2" + UUID.randomUUID().toString().replace("-", "").substring(0, 5);
        } while (studentRepository.existsById(id));
        return id;
    }

}

