package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.dao.GenericDAO;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class GenericServiceImpl<T> implements GenericService<T, UUID> {

    protected final GenericDAO<T> dao;

    protected GenericServiceImpl(GenericDAO<T> dao) {
        this.dao = dao;
    }

    @Override
    public UUID add(T entity) {
        return dao.save(entity);
    }

    @Override
    public void delete(UUID id) {
        Optional<T> entityOptional = dao.findById(id);
        if (entityOptional.isEmpty()) {
            throw new EntityNotFoundException("Entity of type " + getEntityTypeName() + " with id " + id + " does not exist.");
        }
        dao.delete(entityOptional.get());
    }

    @Override
    public void update(T entity) {
        Optional<T> entityOptional = dao.findById(getEntityId(entity));
        if (entityOptional.isEmpty()) {
            throw new EntityNotFoundException("Entity of type " + getEntityTypeName() + " with id " + getEntityId(entity) + " does not exist.");
        }
        dao.update(entity);
    }

    @Override
    public T getById(UUID id) {
        Optional<T> entity = dao.findById(id);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException("Entity of type " + getEntityTypeName() + " with id " + id + " does not exist.");
        }
        return entity.get();
    }

    @Override
    public List<T> getAll() {
        return dao.findAll();
    }

    /**
     * Method to retrieve the entity ID. Must be implemented by subclasses.
     */
    protected abstract UUID getEntityId(T entity);

    /**
     * Method to provide the name of the entity type. Can be overridden for better logs/messages.
     */
    protected String getEntityTypeName() {
        return this.getClass().getSimpleName().replace("Service", "");
    }
}
