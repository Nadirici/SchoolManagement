package servlets.schoolmanagement.services;


import lombok.Data;
import servlets.schoolmanagement.models.Student;
import org.springframework.stereotype.Service;
import servlets.schoolmanagement.repository.StudentRepository;


import lombok.RequiredArgsConstructor;

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
        return studentRepository.existsByEmailAndLastNameAndFirstName(email, lastname, firstname);
    }
}

