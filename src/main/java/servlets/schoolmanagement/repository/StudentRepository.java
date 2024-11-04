package servlets.schoolmanagement.repository;

import org.springframework.lang.NonNull;
import servlets.schoolmanagement.models.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    boolean existsByEmailAndLastNameAndFirstName(String email, String lastName, String firstname);
    boolean existsByEmail(String email);
    boolean existsById( String id);
    Student findByEmail(String email);


    @Override
    Optional<Student> findById( String s);
}
