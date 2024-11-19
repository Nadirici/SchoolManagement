package fr.cyu.schoolmanagementsystem.dao;

import fr.cyu.schoolmanagementsystem.entity.Course;
import org.hibernate.Session;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CourseDAO extends GenericDAO<Course> {
    public CourseDAO(Class<Course> entityClass) {
        super(entityClass);
    }

    public List<Course> findAllNotEnrollByStudentId(UUID studentId) {
        try (Session session = getSession()) {
            String hql = """
            SELECT c
            FROM Course c
            WHERE c.id NOT IN (
                SELECT e.course.id
                FROM Enrollment e
                WHERE e.student.id = :studentId
            )
        """;
            return session.createQuery(hql, Course.class)
                    .setParameter("studentId", studentId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Course> findAllEnrollByStudentId(UUID studentId) {
        try (Session session = getSession()) {
            String hql = """
            SELECT c
            FROM Course c
            WHERE c.id IN (
                SELECT e.course.id
                FROM Enrollment e
                WHERE e.student.id = :studentId
            )
        """;
            return session.createQuery(hql, Course.class)
                    .setParameter("studentId", studentId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
