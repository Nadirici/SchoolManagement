package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.dao.CourseDAO;
import fr.cyu.schoolmanagementsystem.dao.GenericDAO;
import fr.cyu.schoolmanagementsystem.entity.Course;

import java.util.List;
import java.util.UUID;

public class CourseService extends GenericServiceImpl<Course> {

    public CourseService(GenericDAO<Course> dao) {
        super(dao);
    }

    @Override
    protected UUID getEntityId(Course course) {
        return course.getId();
    }

    public List<Course> getAllNotEnroll(UUID studentId) {
        return ((CourseDAO) dao).findAllNotEnrollByStudentId(studentId);
    }

}
