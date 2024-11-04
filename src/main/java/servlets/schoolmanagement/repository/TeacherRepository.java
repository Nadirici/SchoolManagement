package servlets.schoolmanagement.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import servlets.schoolmanagement.models.entity.Teacher;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String> {
    boolean existsByEmailAndLastNameAndFirstName(String email, String lastName, String firstname);
    boolean existsByEmail(String email);
    boolean existsById(String id);
    Teacher findByEmail(String email);


    @NonNull
    @Override
    Optional<Teacher> findById(@NonNull String id);


}
