package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.model.dto.AssignmentDTO;
import fr.cyu.schoolmanagementsystem.model.entity.Assignment;
import fr.cyu.schoolmanagementsystem.repository.AssignmentRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    private final CourseService courseService;

    private final ModelMapper mapper;

    public AssignmentService(AssignmentRepository assignmentRepository, CourseService courseService, ModelMapper mapper) {
        this.assignmentRepository = assignmentRepository;
        this.courseService = courseService;
        this.mapper = mapper;
    }

    @Transactional
    public List<AssignmentDTO> getAllAssignmentsByCourseId(UUID courseId) {
        return assignmentRepository.findAllByCourseId(courseId).stream().map(this::mapToAssignmentDTO).toList();
    }

    public UUID addAssignment(AssignmentDTO assignmentDTO) {
        // TODO: Implementing logic and RuntimeException if needed
        return null;
    }

    public void deleteAssignment(UUID assignmentId) {
        // TODO: Implementing logic and RuntimeException if needed
    }

    public UUID updateAssignment(AssignmentDTO assignmentDTO) {
        // TODO: Implementing logic and RuntimeException if needed
        return null;
    }

    public Optional<AssignmentDTO> getAssignmentById(UUID assignmentId) {
        return assignmentRepository.findById(assignmentId).map(this::mapToAssignmentDTO);
    }

    private AssignmentDTO mapToAssignmentDTO(Assignment assignment) {
        return mapper.map(assignment, AssignmentDTO.class);
    }
}
