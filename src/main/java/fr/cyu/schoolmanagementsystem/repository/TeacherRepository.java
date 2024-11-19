package fr.cyu.schoolmanagementsystem.repository;

import fr.cyu.schoolmanagementsystem.model.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, UUID> {
    Optional<Teacher> findByEmail(String email);
    List<Teacher> findByDepartment(String department);
}
