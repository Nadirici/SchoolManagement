package servlets.schoolmanagement.DAO;

import servlets.schoolmanagement.models.User;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    // Utilisation de merge pour gérer les entités existantes
    public <S extends User> S save(S entity) {
        return entityManager.merge(entity);
    }


    // Sauvegarde une liste d'utilisateurs
    public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
        for (S entity : entities) {
            entityManager.persist(entity);
        }
        return entities;
    }

    // Trouve un utilisateur par ID
    public Optional<User> findById(String id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    // Vérifie si un utilisateur existe par ID
    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    // Trouve tous les utilisateurs
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    // Trouve tous les utilisateurs par leurs IDs
    public List<User> findAllById(Iterable<String> ids) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.id IN :ids", User.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    // Compte le nombre d'utilisateurs
    public long count() {
        return entityManager.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
    }

    // Supprime un utilisateur par ID
    public void deleteById(String id) {
        findById(id).ifPresent(entityManager::remove);
    }

    // Supprime un utilisateur
    public void delete(User entity) {
        if (entityManager.contains(entity)) {
            entityManager.remove(entity);
        } else {
            entityManager.remove(entityManager.merge(entity));
        }
    }

    // Supprime tous les utilisateurs par leurs IDs
    public void deleteAllById(Iterable<String> ids) {
        findAllById(ids).forEach(entityManager::remove);
    }

    // Supprime une liste d'utilisateurs
    public void deleteAll(Iterable<? extends User> entities) {
        for (User entity : entities) {
            delete(entity);
        }
    }

    // Supprime tous les utilisateurs
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM User").executeUpdate();
    }
}
