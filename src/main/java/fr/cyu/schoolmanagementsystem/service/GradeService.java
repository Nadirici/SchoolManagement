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
        // TODO: Implementing logic and RuntimeException
        return null;
    }

    private GradeDTO mapToGradeDTO(Grade grade) {
        return mapper.map(grade, GradeDTO.class);
    }

    public Optional<GradeDTO> getAllGradesByAssignmentIdAndEnrollmentId(UUID id, UUID id1) {
        // TODO: Implementing logic and RuntimeException
        return Optional.empty();
    }
}
