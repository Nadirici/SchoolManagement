package fr.cyu.schoolmanagementsystem.dao;

import fr.cyu.schoolmanagementsystem.entity.Grade;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.UUID;

public class GradeDAO extends GenericDAO<Grade> {
    public GradeDAO(Class<Grade> entityClass) {
        super(entityClass);
    }

    public List<Grade> findAllByEnrollmentId(UUID enrollmentId) {
        try (Session session = getSession()) {
            String hql = "FROM Grade g WHERE g.enrollment.id = :enrollmentId";
            Query<Grade> query = session.createQuery(hql, Grade.class);
            query.setParameter("enrollmentId", enrollmentId);

            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving grades for enrollment with ID: " + enrollmentId, e);
        }
    }

    public List<Grade> findAllByAssignmentId(UUID assignmentId) {
        try (Session session = getSession()) {
            String hql = "FROM Grade g WHERE g.assignment.id = :assignmentId";
            Query<Grade> query = session.createQuery(hql, Grade.class);
            query.setParameter("assignmentId", assignmentId);

            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving grades for assignment with ID: " + assignmentId, e);
        }
    }
}
