package servlets.schoolmanagement.services;

import lombok.Data;
import servlets.schoolmanagement.models.entity.Teacher;
import org.springframework.stereotype.Service;
import servlets.schoolmanagement.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public void registerTeacher(Teacher teacher) {

        teacherRepository.save(teacher); // Sauvegarder l'enseignant
    }

    // Méthode pour vérifier si l'utilisateur existe déjà par email, nom et prénom
    public boolean userExists(String email, String lastname, String firstname) {
        return teacherRepository.existsByEmailAndLastNameAndFirstName(email, lastname, firstname) ||
                teacherRepository.existsByEmail(email);
    }

}
