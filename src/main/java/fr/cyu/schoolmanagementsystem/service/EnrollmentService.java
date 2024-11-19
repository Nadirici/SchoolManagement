package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.dao.EnrollmentDAO;
import fr.cyu.schoolmanagementsystem.dao.GenericDAO;
import fr.cyu.schoolmanagementsystem.dao.GradeDAO;
import fr.cyu.schoolmanagementsystem.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.entity.Grade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EnrollmentService extends GenericServiceImpl<Enrollment> {

    private GradeService gradeService;

    public EnrollmentService(GenericDAO<Enrollment> dao) {
        super(dao);
        gradeService = new GradeService(new GradeDAO(Grade.class));
    }

    @Override
    protected UUID getEntityId(Enrollment enrollment) {
        return enrollment.getId();
    }

    public List<Enrollment> getEnrollmentsForStudent(UUID studentId) {
        return ((EnrollmentDAO) dao).findAllByStudentId(studentId);
    }

    public List<Enrollment> getEnrollmentsForCourse(UUID courseId) {
        return ((EnrollmentDAO) dao).findAllByCourseId(courseId);
    }

    public Map<Enrollment, Grade> getEnrollmentsAndGradesForAssignment(UUID assignmentId) {
        Map<Enrollment, Grade> enrollmentGradeMap = new HashMap<>();

        try {
            List<Grade> grades = gradeService.getAllForAssignment(assignmentId);
            for (Grade grade : grades) {
                Enrollment enrollment = grade.getEnrollment();
                enrollmentGradeMap.put(enrollment, grade);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving stats for assignment with ID: " + assignmentId, e);
        }

        return enrollmentGradeMap;
    }
}
