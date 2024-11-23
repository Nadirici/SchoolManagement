package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.dao.*;
import fr.cyu.schoolmanagementsystem.entity.*;
import fr.cyu.schoolmanagementsystem.service.stats.CourseStatsService;
import jakarta.persistence.EntityNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class StudentService extends GenericServiceImpl<Student> {

    private final EnrollmentService enrollmentService;
    private final GradeService gradeService;

    // Constructeur
    public StudentService(StudentDAO studentDAO, EnrollmentService enrollmentService, GradeService gradeService) {
        super(studentDAO);
        this.enrollmentService = enrollmentService;
        this.gradeService = gradeService;
    }

    @Override
    public UUID add(Student student) {
        Optional<Student> studentOptional = ((StudentDAO) dao).findByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new EntityNotFoundException("A student with this email already exists");
        }
        return dao.save(student);
    }

    public Optional<Student> getStudentByEmail(String email) {
        return ((StudentDAO) dao).findByEmail(email);
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

    public void registerStudent(Student student) {
        // Vérifier si l'étudiant existe déjà par son email
        if (getStudentByEmail(student.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Un étudiant avec cet email existe déjà.");
        }

        // Enregistrer l'étudiant dans la base de données
        dao.save(student);
    }

    public Map<Course, CourseStats> getCourseStats(UUID studentId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsForStudent(studentId);

        // Utilisation d'un Map pour stocker les statistiques par matière (cours)
        Map<Course, CourseStats> courseStatsMap = new HashMap<>();

        for (Enrollment enrollment : enrollments) {
            Course course = enrollment.getCourse();

            // On récupère les notes de l'inscription
            Set<Grade> grades = enrollment.getGrades();

            // On calcule la moyenne des notes
            double totalGrades = 0.0;
            int gradeCount = 0;

            for (Grade grade : grades) {
                if (grade.getScore() != null) {
                    totalGrades += grade.getScore();
                    gradeCount++;
                }
            }

            if (gradeCount > 0) {
                double averageGrade = totalGrades / gradeCount;

                // Vérifier si les statistiques du cours existent déjà, sinon les créer
                CourseStats stats = courseStatsMap.getOrDefault(course, new CourseStats());
                stats.addGrade(averageGrade);  // Ajouter la note dans les statistiques
                courseStatsMap.put(course, stats);  // Mettre à jour la carte avec les nouvelles statistiques
            }
        }

        return courseStatsMap;
    }

    // Classe interne pour gérer les statistiques d'un cours
    public static class CourseStats {
        private List<Double> grades = new ArrayList<>();
        private Double average;
        private Double minGrade;
        private Double maxGrade;

        public void addGrade(Double grade) {
            grades.add(grade);
        }

        public void calculateStats() {
            if (!grades.isEmpty()) {
                average = grades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                minGrade = grades.stream().min(Double::compare).orElse(0.0);
                maxGrade = grades.stream().max(Double::compare).orElse(0.0);
            }
        }

        public Double getAverage() {
            return average;
        }

        public Double getMinGrade() {
            return minGrade;
        }

        public Double getMaxGrade() {
            return maxGrade;
        }
    }
}
