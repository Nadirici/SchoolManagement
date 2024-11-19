package fr.cyu.schoolmanagementsystem.dao;

import fr.cyu.schoolmanagementsystem.entity.Assignment;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.UUID;

public class AssignmentDAO extends GenericDAO<Assignment> {
    public AssignmentDAO(Class<Assignment> entityClass) {
        super(entityClass);
    }

    public List<Assignment> findAllByCourseId(UUID courseId) {
        try (Session session = getSession()) {
            String hql = "from Assignment where course.id = :courseId";
            Query<Assignment> query = session.createQuery(hql, Assignment.class);
            query.setParameter("courseId", courseId);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving assignments for course with ID: " + courseId, e);
        }
    }
}
