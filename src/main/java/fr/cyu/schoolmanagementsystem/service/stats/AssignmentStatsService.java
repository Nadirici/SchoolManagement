package fr.cyu.schoolmanagementsystem.service.stats;

import fr.cyu.schoolmanagementsystem.dao.AssignmentDAO;
import fr.cyu.schoolmanagementsystem.dao.GradeDAO;
import fr.cyu.schoolmanagementsystem.entity.Assignment;
import fr.cyu.schoolmanagementsystem.entity.Grade;
import fr.cyu.schoolmanagementsystem.service.AssignmentService;
import fr.cyu.schoolmanagementsystem.service.GradeService;
import fr.cyu.schoolmanagementsystem.util.CompositeStats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AssignmentStatsService {

    private final GradeService gradeService;
    private final AssignmentService assignmentService;

    public AssignmentStatsService() {
        this.gradeService = new GradeService(new GradeDAO(Grade.class));
        this.assignmentService = new AssignmentService(new AssignmentDAO(Assignment.class));
    }

    public List<Grade> getAllGradesForAssignment(UUID assignmentId) {
        assignmentService.getById(assignmentId);
        return gradeService.getAllForAssignment(assignmentId);
    }

    public double getAverageGradeForAssignment(UUID assignmentId) {
        List<Grade> grades = getAllGradesForAssignment(assignmentId);
        return gradeService.getAverage(grades);
    }

    public double getMinGradeForAssignment(UUID assignmentId) {
        List<Grade> grades = getAllGradesForAssignment(assignmentId);
        return gradeService.getMin(grades);
    }

    public double getMaxGradeForAssignment(UUID assignmentId) {
        List<Grade> grades = getAllGradesForAssignment(assignmentId);
        return gradeService.getMax(grades);
    }

    public CompositeStats getStatsForAssignment(UUID assignmentId) {
        List<Grade> grades = gradeService.getAllForAssignment(assignmentId);
        return gradeService.calculateStats(grades);
    }

    public Map<Assignment, CompositeStats> getAssignmentsStatsMap(UUID courseId) {
        Map<Assignment, CompositeStats> statsMap = new HashMap<>();

        try {
            List<Assignment> assignments = assignmentService.getAssignmentsForCourse(courseId);

            for (Assignment assignment : assignments) {
                try {
                    CompositeStats stats = getStatsForAssignment(assignment.getId());
                    statsMap.put(assignment, stats);
                } catch (Exception e) {
                    statsMap.put(assignment, null);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving stats for course with ID: " + courseId, e);
        }

        return statsMap;
    }
}
