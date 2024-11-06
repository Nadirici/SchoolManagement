package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.model.dto.GradeDTO;
import fr.cyu.schoolmanagementsystem.model.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.model.entity.Grade;
import fr.cyu.schoolmanagementsystem.repository.GradeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public UUID gradeStudent(GradeDTO gradeDTO) {
        Enrollment enrollmentToMap = enrollmentService.findEnrollmentById(gradeDTO.getEnrollmentId());

        Grade grade = mapper.map(gradeDTO, Grade.class);

        grade.setEnrollment(enrollmentToMap);

        gradeRepository.save(grade);

        return grade.getId();
    }

    public void deleteGrade(UUID gradeId) {
        gradeRepository.deleteById(gradeId);
    }

    private GradeDTO mapToGradeDTO(Grade grade) {
        return mapper.map(grade, GradeDTO.class);
    }

    public List<GradeDTO> getAllGradesByEnrollmentId(UUID enrollmentId) {
        return gradeRepository.findAllByEnrollmentId(enrollmentId).stream().map(this::mapToGradeDTO).toList();
    }

    // Get All Grades By Assignment Id
}
