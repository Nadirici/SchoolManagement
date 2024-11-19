package fr.cyu.schoolmanagementsystem.service.stats;

import fr.cyu.schoolmanagementsystem.dao.AssignmentDAO;
import fr.cyu.schoolmanagementsystem.entity.Assignment;
import fr.cyu.schoolmanagementsystem.service.AssignmentService;
import fr.cyu.schoolmanagementsystem.util.CompositeStats;

import java.util.List;
import java.util.UUID;

public class CourseStatsService {

    private final AssignmentStatsService assignmentStatsService;
    private final AssignmentService assignmentService;

    public CourseStatsService() {
        this.assignmentStatsService = new AssignmentStatsService();
        this.assignmentService = new AssignmentService(new AssignmentDAO(Assignment.class));
    }

    public CompositeStats getStatsForCourse(UUID courseId) {
        List<Assignment> assignments = assignmentService.getAssignmentsForCourse(courseId);

        CompositeStats courseStats = new CompositeStats();

        for (Assignment assignment : assignments) {
            CompositeStats assignmentStats = assignmentStatsService.getStatsForAssignment(assignment.getId());
            courseStats.addComponent(assignmentStats);
        }

        return courseStats;
    }
}
