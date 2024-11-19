package fr.cyu.schoolmanagementsystem.dao;

import fr.cyu.schoolmanagementsystem.entity.Admin;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;

public class AdminDAO extends GenericDAO<Admin> {
    public AdminDAO(Class<Admin> entityClass) {
        super(entityClass);
    }

    public Optional<Admin> findByEmail(String adminEmail) {
        try (Session session = getSession()) {
            Query<Admin> query = session.createQuery("from Admin where email = :email", Admin.class);
            query.setParameter("email", adminEmail);

            Admin admin = query.uniqueResult();

            return Optional.ofNullable(admin);
        } catch (Exception e) {
            throw new RuntimeException("Error finding admin by email.", e);
        }
    }
}
