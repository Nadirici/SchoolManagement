package fr.cyu.schoolmanagementsystem.service.stats;

import fr.cyu.schoolmanagementsystem.dao.EnrollmentDAO;
import fr.cyu.schoolmanagementsystem.entity.Assignment;
import fr.cyu.schoolmanagementsystem.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.service.EnrollmentService;
import fr.cyu.schoolmanagementsystem.util.CompositeStats;

import java.util.List;
import java.util.UUID;

public class StudentStatsService {

    private final EnrollmentStatsService enrollmentStatsService;
    private final EnrollmentService enrollmentService;

    public StudentStatsService() {
        this.enrollmentStatsService = new EnrollmentStatsService();
        this.enrollmentService = new EnrollmentService(new EnrollmentDAO(Enrollment.class));
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
