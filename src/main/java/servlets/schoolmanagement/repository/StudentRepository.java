package servlets.schoolmanagement.repository;

import servlets.schoolmanagement.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByEmailAndLastNameAndFirstName(String email, String lastName, String firstname);
    boolean existsByEmail(String email);
    boolean existsById(String id);
}
