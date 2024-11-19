package fr.cyu.schoolmanagementsystem.dao;

import fr.cyu.schoolmanagementsystem.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class GenericDAO<T> implements DAO<T, UUID> {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private final Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected Session getSession() {
        return sessionFactory.openSession();
    }

    @Override
    public UUID save(T e) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            UUID id = (UUID) session.save(e);
            transaction.commit();
            return id;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving entity of type " + entityClass.getSimpleName(), ex);
        }
    }

    @Override
    public void update(T e) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            session.update(e);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error updating entity of type " + entityClass.getSimpleName(), ex);
        }
    }

    @Override
    public void delete(T e) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            session.delete(e);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting entity of type " + entityClass.getSimpleName(), ex);
        }
    }

    @Override
    public Optional<T> findById(UUID id) {
        try (Session session = getSession()) {
            return Optional.ofNullable(session.get(entityClass, id));
        } catch (Exception ex) {
            throw new RuntimeException("Error finding entity of type " + entityClass.getSimpleName() + " with ID: " + id, ex);
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = getSession()) {
            return session.createQuery("FROM " + entityClass.getName(), entityClass).getResultList();
        } catch (Exception ex) {
            throw new RuntimeException("Error finding all entities of type " + entityClass.getSimpleName(), ex);
        }
    }
}