package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.dao.AdminDAO;
import fr.cyu.schoolmanagementsystem.dao.GenericDAO;
import fr.cyu.schoolmanagementsystem.dao.TeacherDAO;
import fr.cyu.schoolmanagementsystem.entity.Admin;
import fr.cyu.schoolmanagementsystem.entity.Teacher;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;
import java.util.UUID;

public class AdminService extends GenericServiceImpl<Admin> {

    protected AdminService(GenericDAO<Admin> dao) {
        super(dao);
    }

    public Admin getByEmail(String email) {
        Optional<Admin> adminOptional = ((AdminDAO) dao).findByEmail(email);
        if (adminOptional.isEmpty()) {
            throw new EntityNotFoundException("Entity of type " + getEntityTypeName() + " with email " + email + " does not exist.");
        }
        return adminOptional.get();
    }
    public static AdminService createInstance(AdminDAO adminDAO) {
        return new AdminService(adminDAO);
    }

    @Override
    protected UUID getEntityId(Admin admin) {
        return admin.getId();
    }

    public Admin getAdminByEmail(String email) {
        return getByEmail(email);
    }

    @Override
    public Admin getById(UUID id) {
        return super.getById(id);
    }
}
