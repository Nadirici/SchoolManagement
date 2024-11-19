package fr.cyu.schoolmanagementsystem.dao;

import fr.cyu.schoolmanagementsystem.entity.Student;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;
public class StudentDAO extends GenericDAO<Student> {

    public StudentDAO(Class<Student> entityClass) {
        super(entityClass);
    }

    public Optional<Student> findByEmail(String studentEmail) {
        try (Session session = getSession()) {
            Query<Student> query = session.createQuery("from Student where email = :email", Student.class);
            query.setParameter("email", studentEmail);

            Student student = query.uniqueResult();

            return Optional.ofNullable(student);
        } catch (Exception e) {
            throw new RuntimeException("Error finding student by email.", e);
        }
    }

    public List<Student> findAllByVerified(boolean verifiedStatus) {
        try (Session session = getSession()) {
            Query<Student> query = session.createQuery("FROM Student s WHERE s.isVerified = :verifiedStatus", Student.class);
            query.setParameter("verifiedStatus", verifiedStatus);

            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error finding verified students.", e);
        }
    }
}
