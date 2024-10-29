package servlets.schoolmanagement.repository;

import servlets.schoolmanagement.models.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servlets.schoolmanagement.models.entity.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String> {
    boolean existsByEmailAndLastNameAndFirstName(String email, String lastName, String firstname);
    boolean existsByEmail(String email);
    boolean existsById(String id);
}
