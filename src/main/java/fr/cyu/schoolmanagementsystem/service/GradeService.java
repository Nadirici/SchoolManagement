package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.model.dto.AssignmentDTO;
import fr.cyu.schoolmanagementsystem.model.dto.EnrollmentDTO;
import fr.cyu.schoolmanagementsystem.model.dto.GradeDTO;
import fr.cyu.schoolmanagementsystem.model.entity.Assignment;
import fr.cyu.schoolmanagementsystem.model.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.model.entity.Grade;
import fr.cyu.schoolmanagementsystem.repository.GradeRepository;
import jakarta.transaction.Transactional;
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

    public void deleteGradesByAssignmentId(UUID assignmentId) {

        List<Grade> grades= gradeRepository.findAllByAssignmentId(assignmentId);
        for (Grade grade: grades){
            gradeRepository.deleteById(grade.getId());
        }
    }

@Transactional
    public UUID updateGrade(GradeDTO gradeDTO) {
        if (gradeRepository.findById(gradeDTO.getId()).isEmpty()) {
            throw new RuntimeException("A grade with this id doesn't exists.");
        }
        Grade grade = mapper.map(gradeDTO, Grade.class);
        gradeRepository.save(grade);
        return grade.getId();
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
            return Double.NaN; // Si aucun grade n'est disponible, la moyenne est 0.
        }

        double totalWeightedScore = 0.0;
        double totalCoefficients = 0.0;

        for (Grade grade : grades) {
            // Chaque grade est associé à un assignment via l'enrollment
            double coefficient = grade.getAssignment().getCoefficient();
            totalWeightedScore += grade.getScore() * coefficient;
            totalCoefficients += coefficient;
        }
        // Calcul de la moyenne pondérée
        if (totalCoefficients == 0) {
            // Si les coefficients sont nuls, cela signifie qu'il n'y a pas de coefficient à appliquer.
            // Vous pouvez soit retourner NaN, soit une moyenne par défaut.
            return Double.NaN;
        }

        return totalWeightedScore / totalCoefficients;
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

    public double calculateAverageGradeForCourse(UUID courseId) {
        // Récupérer la liste des assignments associés à ce cours
        List<AssignmentDTO> assignments = assignmentService.getAllAssignmentsByCourseId(courseId);

        if (assignments.isEmpty()) {
            return 0.0; // Si aucun assignment n'est disponible, la moyenne est 0.
        }

        double totalWeightedAverage = 0.0;
        double totalCoefficients = 0.0;

        // Calculer la moyenne de chaque assignment et la pondérer, en excluant les NaN
        for (AssignmentDTO assignment : assignments) {
            double assignmentAverage = calculateAverageGradeForAssignment(assignment.getId());
            if (!Double.isNaN(assignmentAverage)) { // Vérifier que la moyenne est valide
                double coefficient = assignment.getCoefficient();
                totalWeightedAverage += assignmentAverage * coefficient;
                totalCoefficients += coefficient;
            }
        }

        // Retourner la moyenne pondérée du cours
        return (totalCoefficients > 0) ? (totalWeightedAverage / totalCoefficients) : 0.0;
    }


    public double getMinGradeForAssignment(UUID assignmentId) {
        List<Grade> grades = gradeRepository.findAllByAssignmentId(assignmentId);

        return grades.stream()
                .map(Grade::getScore)
                .filter(score -> !Double.isNaN(score)) // Exclure les NaN
                .min(Double::compare)
                .orElse(0.0); // Si aucun grade valide n'est disponible, retourner 0.
    }

    // Récupérer la note maximale d'un assignment en excluant les NaN
    public double getMaxGradeForAssignment(UUID assignmentId) {
        List<Grade> grades = gradeRepository.findAllByAssignmentId(assignmentId);

        return grades.stream()
                .map(Grade::getScore)
                .filter(score -> !Double.isNaN(score)) // Exclure les NaN
                .max(Double::compare)
                .orElse(0.0); // Si aucun grade valide n'est disponible, retourner 0.
    }

    public double getMinAverageForCourse(UUID courseId) {
        // Récupérer la liste des inscriptions pour ce cours
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);

        if (enrollments.isEmpty()) {
            return 0.0; // Si aucun étudiant n'est inscrit, la moyenne minimale est 0.
        }

        // Calculer la moyenne pondérée des notes pour chaque étudiant, en excluant les NaN
        return enrollments.stream()
                .map(enrollment -> calculateAverageGradeForEnrollment(enrollment.getId()))
                .filter(average -> !Double.isNaN(average)) // Exclure les NaN
                .min(Double::compare)
                .orElse(0.0); // Si aucun grade valide n'est disponible, retourner 0.
    }

    public double getMaxAverageForCourse(UUID courseId) {
        // Récupérer la liste des inscriptions pour ce cours
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);

        if (enrollments.isEmpty()) {
            return 0.0; // Si aucun étudiant n'est inscrit, la moyenne maximale est 0.
        }

        // Calculer la moyenne pondérée des notes pour chaque étudiant, en excluant les NaN
        return enrollments.stream()
                .map(enrollment -> calculateAverageGradeForEnrollment(enrollment.getId()))
                .filter(average -> !Double.isNaN(average)) // Exclure les NaN
                .max(Double::compare)
                .orElse(0.0); // Si aucun grade valide n'est disponible, retourner 0.
    }


// Calculate the global average of a student
    public double calculateAverageGradeForStudent(UUID studentId) {
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);

        if (enrollments.isEmpty()) {
            return 0.0; // Si aucun grade n'est disponible, la moyenne est 0.
        }

        double totalAverage = 0.0;
        int validEnrollmentsCount = 0;

        for (EnrollmentDTO enrollment : enrollments) {
            double averageEnrollment = this.calculateAverageGradeForEnrollment(enrollment.getId());
            if (!Double.isNaN(averageEnrollment)) { // Vérifie si la note est valide
                totalAverage += averageEnrollment;
                validEnrollmentsCount++;
            }
        }

        if (validEnrollmentsCount == 0) {
            return 0.0; // Si aucune note valide n'est disponible, la moyenne est 0.
        }

        return totalAverage / validEnrollmentsCount; // Moyenne des notes pour cet étudiant
    }


    public Optional<GradeDTO> getAllGradesByAssignmentIdAndEnrollmentId(UUID assignmentId, UUID enrollmentId) {
        return gradeRepository.findByAssignmentIdAndEnrollmentId(assignmentId, enrollmentId).map(this::mapToGradeDTO);
    }
}
