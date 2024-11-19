package fr.cyu.schoolmanagementsystem.dao;

import fr.cyu.schoolmanagementsystem.entity.Teacher;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class TeacherDAO extends GenericDAO<Teacher> {
    public TeacherDAO(Class<Teacher> entityClass) {
        super(entityClass);
    }

    public Optional<Teacher> findByEmail(String studentEmail) {
        try (Session session = getSession()) {
            Query<Teacher> query = session.createQuery("from Teacher where email = :email", Teacher.class);
            query.setParameter("email", studentEmail);

            Teacher teacher = query.uniqueResult();

            return Optional.ofNullable(teacher);
        } catch (Exception e) {
            throw new RuntimeException("Error finding teacher by email.", e);
        }
    }

    public List<Teacher> findAllByVerified(boolean verifiedStatus) {
        try (Session session = getSession()) {
            Query<Teacher> query = session.createQuery("FROM Teacher s WHERE s.isVerified = :verifiedStatus", Teacher.class);
            query.setParameter("verifiedStatus", verifiedStatus);

            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error finding verified students.", e);
        }
    }
}
