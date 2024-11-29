package fr.cyu.schoolmanagementsystem.entity;

import fr.cyu.schoolmanagementsystem.entity.base.BaseEntity;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course extends BaseEntity {

    private String name;

    private String description;


    private DayOfWeek dayOfWeek; // Utilisation explicite de java.time.DayOfWeek // e.g., "Monday", "Tuesday"

    private LocalDateTime startTime;
    private LocalDateTime endTime;   // Heure de fin du cours

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Enrollment> enrollments;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Assignment> assignments;

    public Course() {
        enrollments = new HashSet<>();
        assignments = new HashSet<>();
    }

    // Méthodes d'ajout et de suppression pour les enrollments et assignments

    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public void removeAssignment(Assignment assignment) {
        assignments.remove(assignment);
    }

    // Getters et setters pour les nouveaux champs
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime  getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime  endTime) {
        this.endTime = endTime;
    }

    // Getters et setters existants
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Set<Assignment> getAssignments() {
        return assignments;
    }

    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }
}
