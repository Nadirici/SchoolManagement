package fr.cyu.schoolmanagementsystem.repository;

import fr.cyu.schoolmanagementsystem.model.entity.Grade;
import fr.cyu.schoolmanagementsystem.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GradeRepository extends JpaRepository<Grade, UUID> {
    List<Grade> findAllByEnrollmentId(UUID enrollmentId);
    List<Grade> findAllByAssignmentId(UUID assignmentId);
    Optional<Grade> findByAssignmentIdAndEnrollmentId(UUID assignmentId, UUID enrollmentId);
    @Transactional
    void deleteByEnrollmentId(UUID enrollmentId);



}
