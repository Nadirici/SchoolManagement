package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.dao.*;
import fr.cyu.schoolmanagementsystem.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.entity.Student;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class StudentService extends GenericServiceImpl<Student> {

    private final EnrollmentService enrollmentService;

    public StudentService(StudentDAO studentDAO) {
        super(studentDAO);
        enrollmentService = new EnrollmentService(new EnrollmentDAO(Enrollment.class));
    }

    @Override
    public UUID add(Student student) {
        Optional<Student> studentOptional = ((StudentDAO) dao).findByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new EntityNotFoundException("A student with this email already exists");
        }
        return dao.save(student);
    }

    public List<Student> getAllVerified() {
        return ((StudentDAO) dao).findAllByVerified(true);
    }

    @Override
    protected UUID getEntityId(Student student) {
        return student.getId();
    }

    public List<Student> getAllStudentsNotEnrollInCourse(UUID courseId) {
        List<Student> students = getAllVerified();
        try {
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsForCourse(courseId);

            Set<UUID> enrolledStudentIds = enrollments.stream()
                    .map(enrollment -> enrollment.getStudent().getId())
                    .collect(Collectors.toSet());

            return students.stream()
                    .filter(student -> !enrolledStudentIds.contains(student.getId()))
                    .collect(Collectors.toList());
        } catch(EntityNotFoundException e) {
            throw new EntityNotFoundException("No student with this id exists");
        }
    }
}
