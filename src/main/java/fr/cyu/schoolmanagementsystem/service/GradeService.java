package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.model.dto.AssignmentDTO;
import fr.cyu.schoolmanagementsystem.model.dto.EnrollmentDTO;
import fr.cyu.schoolmanagementsystem.model.dto.GradeDTO;
import fr.cyu.schoolmanagementsystem.model.entity.Assignment;
import fr.cyu.schoolmanagementsystem.model.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.model.entity.Grade;
import fr.cyu.schoolmanagementsystem.repository.GradeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;

    private final EnrollmentService enrollmentService;

    private final AssignmentService assignmentService;

    private final ModelMapper mapper;

    @Autowired
    public GradeService(GradeRepository gradeRepository, EnrollmentService enrollmentService, AssignmentService assignmentService, ModelMapper mapper) {
        this.gradeRepository = gradeRepository;
        this.enrollmentService = enrollmentService;
        this.assignmentService = assignmentService;
        this.mapper = mapper;
    }

    public UUID addGrade(GradeDTO gradeDTO) {
        Optional<EnrollmentDTO> enrollment = enrollmentService.getEnrollmentById(gradeDTO.getEnrollmentId());
        Optional<AssignmentDTO> assignment = assignmentService.getAssignmentById(gradeDTO.getAssignmentId());

        if (enrollment.isEmpty() || assignment.isEmpty()) {
            throw new RuntimeException("Can't add grade because enrollment or assignment not found");
        }

        Grade grade = mapper.map(gradeDTO, Grade.class);

        Enrollment e = mapper.map(enrollment.get(), Enrollment.class);
        Assignment a = mapper.map(assignment.get(), Assignment.class);

        grade.setEnrollment(e);
        grade.setAssignment(a);
        grade.setScore(gradeDTO.getScore());

        gradeRepository.save(grade);

        return grade.getId();
    }

    public void deleteGrade(UUID gradeId) {
        gradeRepository.deleteById(gradeId);
    }

    public UUID updateGrade(UUID gradeId, GradeDTO gradeDTO) {
        // TODO: Implementing logic and RuntimeException
        return null;
    }

    public List<GradeDTO> getAllGradesByEnrollmentId(UUID enrollmentId) {
        return gradeRepository.findAllByEnrollmentId(enrollmentId).stream().map(this::mapToGradeDTO).toList();
    }

    public List<GradeDTO> getGradesByAssignmentId(UUID assignmentId) {
        return gradeRepository.findAllByAssignmentId(assignmentId).stream().map(this::mapToGradeDTO).toList();
    }

    private GradeDTO mapToGradeDTO(Grade grade) {
        return mapper.map(grade, GradeDTO.class);
    }

    //calculate the average grade for a student enrolled in a certain course
    public double calculateAverageGradeForEnrollment(UUID enrollmentId) {
        // Récupérer les grades associés à un étudiant inscrit dans ce cours
        List<Grade> grades = gradeRepository.findAllByEnrollmentId(enrollmentId);

        if (grades.isEmpty()) {
            return 0.0; // Si aucun grade n'est disponible, la moyenne est 0.
        }

        double totalWeightedScore = 0.0;
        double totalCoefficients = 0.0;

        for (Grade grade : grades) {
            // Chaque grade est associé à un assignment via l'enrollment
            double coefficient = grade.getAssignment().getCoefficient();
            totalWeightedScore += grade.getScore() * coefficient;
            totalCoefficients += coefficient;
        }


        return (totalCoefficients > 0) ? totalWeightedScore / grades.size() : 0.0; // Moyenne des notes pour cet étudiant dans ce cours
    }

    //calculate the class average of an assignment in a course
    public double calculateAverageGradeForAssignment(UUID assignmentId) {
        List<Grade> grades = gradeRepository.findAllByAssignmentId(assignmentId);

        if (grades.isEmpty()) {
            return 0.0; // Si aucun grade n'est disponible, la moyenne est 0.
        }

        double totalScore = 0.0;
        for (Grade grade : grades) {
            totalScore += grade.getScore();
        }

        return totalScore / grades.size(); // Moyenne des notes pour cet assignment
    }

    //calculate the class average grade for a course
    public double calculateAverageGradeForCourse(UUID courseId) {
        // Récupérer la liste des assignments associés à ce cours
        List<AssignmentDTO> assignments = assignmentService.getAllAssignmentsByCourseId(courseId);

        if (assignments.isEmpty()) {
            return 0.0; // Si aucun assignment n'est disponible, la moyenne est 0.
        }

        double totalWeightedAverage = 0.0;
        double totalCoefficients = 0.0;

        // Calculer la moyenne de chaque assignment et la pondérer
        for (AssignmentDTO assignment : assignments) {
            double assignmentAverage = calculateAverageGradeForAssignment(assignment.getId());
            double coefficient = assignment.getCoefficient();

            totalWeightedAverage += assignmentAverage * coefficient;
            totalCoefficients += coefficient;
        }

        // Retourner la moyenne pondérée du cours
        return (totalCoefficients > 0) ? (totalWeightedAverage / totalCoefficients) : 0.0;
    }

    public double getMinGradeForAssignment(UUID assignmentId) {
        List<Grade> grades = gradeRepository.findAllByAssignmentId(assignmentId);

        return grades.stream()
                .min(Comparator.comparingDouble(Grade::getScore))
                .map(Grade::getScore)
                .orElse(0.0); // Si aucun grade n'est disponible, retourner 0.
    }

    // Récupérer la note maximale d'un assignment
    public double getMaxGradeForAssignment(UUID assignmentId) {
        List<Grade> grades = gradeRepository.findAllByAssignmentId(assignmentId);

        return grades.stream()
                .max(Comparator.comparingDouble(Grade::getScore))
                .map(Grade::getScore)
                .orElse(0.0); // Si aucun grade n'est disponible, retourner 0.
    }
    public double getMinAverageForCourse(UUID courseId) {
        // Récupérer la liste des inscriptions pour ce cours
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);

        if (enrollments.isEmpty()) {
            return 0.0; // Si aucun étudiant n'est inscrit, la moyenne minimale est 0.
        }

        // Calculer la moyenne pondérée des notes pour chaque étudiant
        return enrollments.stream()
                .map(enrollment -> calculateAverageGradeForEnrollment(enrollment.getId()))
                .min(Double::compare)
                .orElse(0.0); // Si aucun grade n'est disponible, retourner 0.
    }

    public double getMaxAverageForCourse(UUID courseId) {
        // Récupérer la liste des inscriptions pour ce cours
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);

        if (enrollments.isEmpty()) {
            return 0.0; // Si aucun étudiant n'est inscrit, la moyenne maximale est 0.
        }

        // Calculer la moyenne pondérée des notes pour chaque étudiant
        return enrollments.stream()
                .map(enrollment -> calculateAverageGradeForEnrollment(enrollment.getId()))
                .max(Double::compare)
                .orElse(0.0); // Si aucun grade n'est disponible, retourner 0.
    }

    //calculate the global average of a student
    public double calculateAverageGradeForStudent(UUID studentId) {

        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);

        if (enrollments.isEmpty()) {
            return 0.0; // Si aucun grade n'est disponible, la moyenne est 0.
        }
        double totalAverage=0.0;
        for (EnrollmentDTO enrollment : enrollments) {
            double averageEnrollment= this.calculateAverageGradeForEnrollment(enrollment.getId());
            totalAverage+=averageEnrollment;
        }


        return totalAverage / enrollments.size() ; // Moyenne des notes pour cet étudiant dans ce cours
    }

    public Optional<GradeDTO> getAllGradesByAssignmentIdAndEnrollmentId(UUID assignmentId, UUID enrollmentId) {
        return gradeRepository.findByAssignmentIdAndEnrollmentId(assignmentId, enrollmentId).map(this::mapToGradeDTO);
    }
}
