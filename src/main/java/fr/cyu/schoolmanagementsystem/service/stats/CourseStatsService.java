package fr.cyu.schoolmanagementsystem.service.stats;

import fr.cyu.schoolmanagementsystem.dao.AssignmentDAO;
import fr.cyu.schoolmanagementsystem.dao.CourseDAO;
import fr.cyu.schoolmanagementsystem.dao.EnrollmentDAO;
import fr.cyu.schoolmanagementsystem.entity.Assignment;
import fr.cyu.schoolmanagementsystem.entity.Course;
import fr.cyu.schoolmanagementsystem.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.service.AssignmentService;
import fr.cyu.schoolmanagementsystem.service.CourseService;
import fr.cyu.schoolmanagementsystem.service.EnrollmentService;
import fr.cyu.schoolmanagementsystem.util.CompositeStats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CourseStatsService {

    private final AssignmentStatsService assignmentStatsService;
    private final AssignmentService assignmentService;
    private final EnrollmentStatsService enrollmentStatsService;
    private final EnrollmentService enrollmentService;
    private final CourseService courseService;

    public CourseStatsService() {
        this.assignmentStatsService = new AssignmentStatsService();
        this.assignmentService = new AssignmentService(new AssignmentDAO(Assignment.class));
        this.enrollmentStatsService = new EnrollmentStatsService();
        this.enrollmentService = new EnrollmentService(new EnrollmentDAO(Enrollment.class));
        this.courseService = new CourseService(new CourseDAO(Course.class));
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

/*    public Map<Course, CompositeStats> getCourseStatsForStudent(UUID studentId) {

        Map<Course, CompositeStats> map = new HashMap<>();

        // List<Map<Enrollment, CompositeStats>>

        // for (Map<Enrollment, CompositeStats> in List<Map<Enrollment, CompositeStats>>
        //      CompositeStats stats = getStatsForCourse(course);

        for (Course course: courses) {

            // Map<Enrollment, CompositeStats> getEnrollmentsStatsMap(UUID courseId)
            // On récupère un CompositeStats avec moyenne, min et max d'un étudiant
            CompositeStats studentsStats = getStatsForStudent(studentId);
        }

        return map;
    }*/
}
