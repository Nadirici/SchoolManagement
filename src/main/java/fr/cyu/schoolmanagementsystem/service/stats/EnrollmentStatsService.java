package fr.cyu.schoolmanagementsystem.service.stats;

import fr.cyu.schoolmanagementsystem.dao.EnrollmentDAO;
import fr.cyu.schoolmanagementsystem.dao.GradeDAO;
import fr.cyu.schoolmanagementsystem.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.entity.Grade;
import fr.cyu.schoolmanagementsystem.service.EnrollmentService;
import fr.cyu.schoolmanagementsystem.service.GradeService;
import fr.cyu.schoolmanagementsystem.util.CompositeStats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EnrollmentStatsService {

    private final GradeService gradeService;
    private final EnrollmentService enrollmentService;

    public EnrollmentStatsService() {
        this.gradeService = new GradeService(new GradeDAO(Grade.class));
        this.enrollmentService = new EnrollmentService(new EnrollmentDAO(Enrollment.class));
    }

    public List<Grade> getAllGradesForEnrollment(UUID enrollmentId) {
        enrollmentService.getById(enrollmentId);
        return gradeService.getAllForEnrollment(enrollmentId);
    }

    public double getAverageGradeForEnrollment(UUID enrollmentId) {
        List<Grade> grades = getAllGradesForEnrollment(enrollmentId);
        return gradeService.getAverage(grades);
    }

    public double getMinGradeForEnrollment(UUID enrollmentId) {
        List<Grade> grades = getAllGradesForEnrollment(enrollmentId);
        return gradeService.getMin(grades);
    }

    public double getMaxGradeForEnrollment(UUID enrollmentId) {
        List<Grade> grades = getAllGradesForEnrollment(enrollmentId);
        return gradeService.getMax(grades);
    }

    public CompositeStats getStatsForEnrollment(UUID enrollmentId) {
        List<Grade> grades = gradeService.getAllForEnrollment(enrollmentId);
        return gradeService.calculateStats(grades);
    }

    public Map<Enrollment, CompositeStats> getEnrollmentsStatsMap(UUID courseId) {
        Map<Enrollment, CompositeStats> statsMap = new HashMap<>();

        try {
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsForCourse(courseId);

            for (Enrollment enrollment : enrollments) {
                try {
                    CompositeStats stats = getStatsForEnrollment(enrollment.getId());
                    statsMap.put(enrollment, stats);
                } catch (Exception e) {
                    statsMap.put(enrollment, null);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving stats for course with ID: " + courseId, e);
        }

        return statsMap;
    }
}
