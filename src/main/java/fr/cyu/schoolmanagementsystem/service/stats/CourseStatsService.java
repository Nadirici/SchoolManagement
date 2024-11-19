package fr.cyu.schoolmanagementsystem.service.stats;

import fr.cyu.schoolmanagementsystem.dao.AssignmentDAO;
import fr.cyu.schoolmanagementsystem.dao.EnrollmentDAO;
import fr.cyu.schoolmanagementsystem.entity.Assignment;
import fr.cyu.schoolmanagementsystem.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.service.AssignmentService;
import fr.cyu.schoolmanagementsystem.service.EnrollmentService;
import fr.cyu.schoolmanagementsystem.util.CompositeStats;

import java.util.List;
import java.util.UUID;

public class CourseStatsService {

    private final AssignmentStatsService assignmentStatsService;
    private final AssignmentService assignmentService;
    private final EnrollmentStatsService enrollmentStatsService;
    private final EnrollmentService enrollmentService;

    public CourseStatsService() {
        this.assignmentStatsService = new AssignmentStatsService();
        this.assignmentService = new AssignmentService(new AssignmentDAO(Assignment.class));
        this.enrollmentStatsService = new EnrollmentStatsService();
        this.enrollmentService = new EnrollmentService(new EnrollmentDAO(Enrollment.class));
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

    public CompositeStats getStatsForStudent(UUID studentId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsForStudent(studentId);

        CompositeStats studentStats = new CompositeStats();

        for (Enrollment enrollment : enrollments) {
            CompositeStats enrollmentStats = enrollmentStatsService.getStatsForEnrollment(enrollment.getId());
            studentStats.addComponent(enrollmentStats);
        }

        return studentStats;
    }
}
