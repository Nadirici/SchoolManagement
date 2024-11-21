package fr.cyu.schoolmanagementsystem.repository;

import fr.cyu.schoolmanagementsystem.model.entity.Grade;
import fr.cyu.schoolmanagementsystem.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GradeRepository extends JpaRepository<Grade, UUID> {
    List<Grade> findAllByEnrollmentId(UUID enrollmentId);
    List<Grade> findAllByAssignmentId(UUID assignmentId);
    Optional<Grade> findByAssignmentIdAndEnrollmentId(UUID assignmentId, UUID enrollmentId);
    List<Grade> findAllByAssignmentIdAndEnrollmentId(UUID assignmentId, UUID enrollmentId);


}
