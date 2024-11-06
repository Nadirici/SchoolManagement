package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.repository.AssignmentRepository;
import fr.cyu.schoolmanagementsystem.repository.CourseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    private final CourseService courseService;

    private final ModelMapper modelMapper;

    public AssignmentService(AssignmentRepository assignmentRepository, CourseService courseService, ModelMapper mapper) {
        this.assignmentRepository = assignmentRepository;
        this.courseService = courseService;
        this.modelMapper = mapper;
    }

    // Get Assignment By Course

    // Post Assignment

    // Edit Assignment

    // Delete Assignment
}
