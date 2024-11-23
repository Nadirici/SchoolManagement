package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.dao.GenericDAO;
import fr.cyu.schoolmanagementsystem.dao.StudentDAO;
import fr.cyu.schoolmanagementsystem.dao.TeacherDAO;
import fr.cyu.schoolmanagementsystem.entity.Student;
import fr.cyu.schoolmanagementsystem.entity.Teacher;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TeacherService extends GenericServiceImpl<Teacher> {
    
    public TeacherService(GenericDAO<Teacher> dao) {
        super(dao);
    }

    @Override
    public UUID add(Teacher teacher) {
        Optional<Teacher> teacherOptional = ((TeacherDAO) dao).findByEmail(teacher.getEmail());
        if (teacherOptional.isPresent()) {
            throw new EntityNotFoundException("A teacher with this email already exists");
        }
        return ((TeacherDAO) dao).save(teacher);
    }



    public List<Teacher> getAllVerified() {
        return ((TeacherDAO) dao).findAllByVerified(true);
    }

    @Override
    protected UUID getEntityId(Teacher teacher) {
        return teacher.getId();
    }

    public Optional<Teacher> getTeacherByEmail(String email) {
        return ((TeacherDAO) dao).findByEmail(email);
    }

    public void registerTeacher(Teacher teacher) {
        // Vérifier si l'étudiant existe déjà par son email
        if (getTeacherByEmail(teacher.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà.");
        }

        // Enregistrer l'étudiant dans la base de données
        dao.save(teacher);
    }
}
