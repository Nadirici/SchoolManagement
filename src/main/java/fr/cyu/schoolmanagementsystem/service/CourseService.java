package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.dao.CourseDAO;
import fr.cyu.schoolmanagementsystem.dao.EnrollmentDAO;
import fr.cyu.schoolmanagementsystem.dao.GenericDAO;
import fr.cyu.schoolmanagementsystem.entity.Course;
import fr.cyu.schoolmanagementsystem.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.util.CompositeStats;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CourseService extends GenericServiceImpl<Course> {

    private EnrollmentService enrollmentService;

    public CourseService(GenericDAO<Course> dao) {
        super(dao);
        enrollmentService = new EnrollmentService(new EnrollmentDAO(Enrollment.class));
    }

    @Override
    protected UUID getEntityId(Course course) {
        return course.getId();
    }

    public List<Course> getAllNotEnroll(UUID studentId) {
        return ((CourseDAO) dao).findAllNotEnrollByStudentId(studentId);
    }

    public List<Course> getAllEnroll(UUID studentId) {
        return ((CourseDAO) dao).findAllEnrollByStudentId(studentId);
    }
}
