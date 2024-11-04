package servlets.schoolmanagement.services;

import lombok.Data;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import servlets.schoolmanagement.models.entity.Teacher;
import org.springframework.stereotype.Service;
import servlets.schoolmanagement.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

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

    public boolean userExistsByEmail(String email) {
        return teacherRepository.existsByEmail(email);
    }

    public Teacher findTeacherByEmail(String email) {
        return teacherRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Teacher> findTeacherById(String id) {
        if(teacherRepository.existsById(id)) {
            System.out.println("Il existe !");
        }
        System.out.println("Recherche de l'enseignant avec ID : " + id);
        return teacherRepository.findById(id);
    }






}
