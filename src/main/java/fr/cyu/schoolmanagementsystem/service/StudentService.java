package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.dao.*;
import fr.cyu.schoolmanagementsystem.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.entity.Student;
import fr.cyu.schoolmanagementsystem.service.stats.StudentStatsService;
import fr.cyu.schoolmanagementsystem.util.CompositeStats;
import fr.cyu.schoolmanagementsystem.entity.Course;
import jakarta.persistence.EntityNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class StudentService extends GenericServiceImpl<Student> {

    private final EnrollmentService enrollmentService;
    private StudentStatsService studentStatsService;

    public StudentService(StudentDAO studentDAO) {
        super(studentDAO);
        enrollmentService = new EnrollmentService(new EnrollmentDAO(Enrollment.class));
        studentStatsService = new StudentStatsService();
    }

    @Override
    public UUID add(Student student) {
        Optional<Student> studentOptional = ((StudentDAO) dao).findByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new EntityNotFoundException("A student with this email already exists");
        }
        return dao.save(student);
    }



    public boolean isStudentAvailable(Student student, Course course) {
        // Parcourir tous les cours auxquels l'étudiant est déjà inscrit
        return student.getEnrollments().stream()
                .map(Enrollment::getCourse)
                .noneMatch(enrolledCourse ->
                        enrolledCourse.getDayOfWeek() == course.getDayOfWeek() &&
                                (
                                        (course.getStartTime().isBefore(enrolledCourse.getEndTime()) &&
                                                course.getEndTime().isAfter(enrolledCourse.getStartTime()))
                                )
                );
    }



    public Student getByEmail(String email) {
        Optional<Student> studentOptional = ((StudentDAO) dao).findByEmail(email);
        if (studentOptional.isEmpty()) {
            throw new EntityNotFoundException("Entity of type " + getEntityTypeName() + " with email " + email + " does not exist.");
        }
        return studentOptional.get();
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

    public List<Student> getAllStudentsWithGlobalAverageGreaterThanTen() {
        return getAllVerified().stream()
                .filter(student -> studentStatsService.getStatsForStudent(student.getId()).getAverage() >= 10)
                .collect(Collectors.toList());
    }

    public double getPercentageOfStudentsWithGlobalAverageGreaterThanTen() {
        List<Student> allStudents = getAllVerified();
        long countAboveTen = allStudents.stream()
                .filter(student -> studentStatsService.getStatsForStudent(student.getId()).getAverage() >= 10)
                .count();

        return allStudents.isEmpty() ? 0.0 : (countAboveTen * 100.0) / allStudents.size();
    }
}
