package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.dao.GenericDAO;
import fr.cyu.schoolmanagementsystem.dao.StudentDAO;
import fr.cyu.schoolmanagementsystem.dao.TeacherDAO;
import fr.cyu.schoolmanagementsystem.entity.Course;
import fr.cyu.schoolmanagementsystem.entity.Student;
import fr.cyu.schoolmanagementsystem.entity.Teacher;
import jakarta.persistence.EntityNotFoundException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public boolean isTeacherAvailableForDay(Teacher teacher, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        // Vérifier que les horaires sont valides
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Les horaires de début et de fin doivent être spécifiés.");
        }
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("L'heure de début doit être avant l'heure de fin.");
        }

        // Récupérer les cours assignés au professeur
        Set<Course> courses = teacher.getCourses();
        if (courses == null || courses.isEmpty()) {
            return true; // Pas de cours assignés, le professeur est disponible
        }

        // Vérifier les conflits d'horaires et de jours
        for (Course course : courses) {
            if (course == null || course.getStartTime() == null || course.getEndTime() == null || course.getDayOfWeek() == null) {
                continue; // Ignorer les cours mal définis
            }

            // Vérifier si le jour correspond
            if (course.getDayOfWeek().equals(dayOfWeek)) {
                LocalTime courseStartTime = course.getStartTime().toLocalTime();
                LocalTime courseEndTime = course.getEndTime().toLocalTime();

                // Vérifier si les horaires se chevauchent
                if (startTime.isBefore(courseEndTime) && endTime.isAfter(courseStartTime)) {
                    return false; // Conflit détecté
                }
            }
        }

        return true; // Aucun conflit trouvé
    }



    @Override
    protected UUID getEntityId(Teacher teacher) {
        return teacher.getId();
    }

    public Teacher getByEmail(String email) {
        Optional<Teacher> teacherOptional = ((TeacherDAO) dao).findByEmail(email);
        if (teacherOptional.isEmpty()) {
            throw new EntityNotFoundException("Entity of type " + getEntityTypeName() + " with email " + email + " does not exist.");
        }
        return teacherOptional.get();
    }
}
