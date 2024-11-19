package fr.cyu.schoolmanagementsystem.dao;

import fr.cyu.schoolmanagementsystem.entity.Enrollment;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EnrollmentDAO  extends GenericDAO<Enrollment>{
    public EnrollmentDAO(Class<Enrollment> entityClass) {
        super(entityClass);
    }

    public List<Enrollment> findAllByStudentId(UUID studentId) {
        try (Session session = getSession()) {
            String hql = "FROM Enrollment e WHERE e.student.id = :studentId";
            Query<Enrollment> query = session.createQuery(hql, Enrollment.class);
            query.setParameter("studentId", studentId);

            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving enrollments for student with ID: " + studentId, e);
        }
    }

    public List<Enrollment> findAllByCourseId(UUID courseId) {
        try (Session session = getSession()) {
            String hql = "FROM Enrollment e WHERE e.course.id = :courseId";
            Query<Enrollment> query = session.createQuery(hql, Enrollment.class);
            query.setParameter("courseId", courseId);

            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving enrollments for student with ID: " + courseId, e);
        }
    }

    public Optional<Enrollment> findByStudentIdAndCourseId(UUID studentId, UUID courseId) {
        try (Session session = getSession()) {
            String hql = "FROM Enrollment e WHERE e.student.id = :studentId AND e.course.id = :courseId";
            Query<Enrollment> query = session.createQuery(hql, Enrollment.class);
            query.setParameter("studentId", studentId);
            query.setParameter("courseId", courseId);

            Enrollment enrollment = query.uniqueResult();
            return Optional.ofNullable(enrollment);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving enrollment for student with ID: " + studentId + " and course with ID: " + courseId, e);
        }
    }
}
