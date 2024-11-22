package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.model.dto.AssignmentDTO;
import fr.cyu.schoolmanagementsystem.model.dto.CourseDTO;
import fr.cyu.schoolmanagementsystem.model.entity.Assignment;
import fr.cyu.schoolmanagementsystem.model.entity.Course;
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
@Transactional
    public UUID addAssignment(AssignmentDTO assignmentDTO, UUID courseId) {
        Optional<CourseDTO> courseDTO = courseService.getCourseById(courseId);

        if (courseDTO.isEmpty()) {
            throw new RuntimeException("A course with this id does not exist.");
        }

        Assignment assignment = mapper.map(assignmentDTO, Assignment.class);

        Course c = mapper.map(courseDTO.get(), Course.class);

        assignment.setCourse(c);

        assignmentRepository.save(assignment);
        assignmentRepository.flush();

        return assignment.getId();
    }
@Transactional
    public void deleteAssignment(UUID assignmentId) {
        if (assignmentRepository.findById(assignmentId).isEmpty()) {
            throw new RuntimeException("Assignment with this id does not exist.");
        }

        assignmentRepository.deleteById(assignmentId);
    }
@Transactional
    public UUID updateAssignment(AssignmentDTO assignmentDTO) {
        if (assignmentRepository.findById(assignmentDTO.getId()).isEmpty()) {
            throw new RuntimeException("An assignment with this id doesn't exists.");
        }
        Assignment assignment = mapper.map(assignmentDTO, Assignment.class);
        assignmentRepository.save(assignment);
        return assignment.getId();
    }

    public Optional<AssignmentDTO> getAssignmentById(UUID assignmentId) {
        return assignmentRepository.findById(assignmentId).map(this::mapToAssignmentDTO);
    }

    private AssignmentDTO mapToAssignmentDTO(Assignment assignment) {
        return mapper.map(assignment, AssignmentDTO.class);
    }
}
