package fr.cyu.schoolmanagementsystem.dao;

import fr.cyu.schoolmanagementsystem.entity.RegistrationRequest;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.UUID;

public class RegistrationRequestDAO extends GenericDAO<RegistrationRequest> {
    public RegistrationRequestDAO(Class<RegistrationRequest> entityClass) {
        super(entityClass);
    }

    public List<RegistrationRequest> findByStatusFalse() {
        try (Session session = getSession()) {
            String hql = "FROM RegistrationRequest r WHERE r.status = false";
            Query<RegistrationRequest> query = session.createQuery(hql, RegistrationRequest.class);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving pending registration requests", e);
        }
    }

    public List<RegistrationRequest> findByTeacherIdIsNotNullAndStudentIdIsNullAndStatusFalse() {
        try (Session session = getSession()) {
            String hql = "FROM RegistrationRequest r WHERE r.teacher.id IS NOT NULL AND r.student.id IS NULL AND r.status = false";
            Query<RegistrationRequest> query = session.createQuery(hql, RegistrationRequest.class);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving pending registration requests for teacher", e);
        }
    }

    public List<RegistrationRequest> findByStudentIdIsNotNullAndTeacherIdIsNullAndStatusFalse() {
        try (Session session = getSession()) {
            String hql = "FROM RegistrationRequest r WHERE r.student.id IS NOT NULL AND r.teacher.id IS NULL AND r.status = false";
            Query<RegistrationRequest> query = session.createQuery(hql, RegistrationRequest.class);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving pending registration requests for student", e);
        }
    }

    public List<RegistrationRequest> findByStudentId(UUID studentId) {
        try (Session session = getSession()) {
            String hql = "FROM RegistrationRequest r WHERE r.student.id = :studentId";
            Query<RegistrationRequest> query = session.createQuery(hql, RegistrationRequest.class);
            query.setParameter("studentId", studentId);
            return query.list();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving registration requests for student with ID: " + studentId, e);
        }
    }
}
