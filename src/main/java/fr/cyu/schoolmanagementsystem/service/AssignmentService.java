package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.dao.AssignmentDAO;
import fr.cyu.schoolmanagementsystem.dao.GenericDAO;
import fr.cyu.schoolmanagementsystem.dao.GradeDAO;
import fr.cyu.schoolmanagementsystem.entity.Assignment;
import fr.cyu.schoolmanagementsystem.entity.Grade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AssignmentService extends GenericServiceImpl<Assignment> {

    private GradeService gradeService;

    public AssignmentService(GenericDAO<Assignment> dao) {
        super(dao);
        gradeService = new GradeService(new GradeDAO(Grade.class));
    }

    @Override
    protected UUID getEntityId(Assignment assignment) {
        return assignment.getId();
    }

    public List<Assignment> getAssignmentsForCourse(UUID courseId) {
        return ((AssignmentDAO) dao).findAllByCourseId(courseId);
    }

    public Map<Assignment, Grade> getAssignmentsAndGradesForEnrollment(UUID enrollmentId) {
        Map<Assignment, Grade> assignmentGradeMap = new HashMap<>();

        try {
            List<Grade> grades = gradeService.getAllForEnrollment(enrollmentId);
            for (Grade grade : grades) {
                Assignment assignment = grade.getAssignment();
                assignmentGradeMap.put(assignment, grade);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving stats for enrollment with ID: " + enrollmentId, e);
        }

        return assignmentGradeMap;
    }
}
