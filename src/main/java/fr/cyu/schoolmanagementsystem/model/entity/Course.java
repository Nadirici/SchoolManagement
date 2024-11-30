package fr.cyu.schoolmanagementsystem.model.entity;

import fr.cyu.schoolmanagementsystem.model.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@Entity
@Getter
@Table(name = "courses")
public class Course extends BaseEntity {

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Column(name = "description")
    private String description;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private Set<Enrollment> enrollments;

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private Set<Assignment> assignments;


    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek; // Jour de la semaine (Lundi à Vendredi)

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }


    @Column(name = "start_time", nullable = false)
    private LocalTime startTime; // Heure de début du cours

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime; // Heure de fin du cours

    // Ne pas supprimer car pas reconnu avec setter dans AdminWebController
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Course() {
        enrollments = new HashSet<>();
        assignments = new HashSet<>();
    }

    public Course(String name, String description, Teacher teacher, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.description = description;
        this.teacher = teacher;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getFrenchDayOfWeek() {
        switch (dayOfWeek) {
            case MONDAY: return "Lundi";
            case TUESDAY: return "Mardi";
            case WEDNESDAY: return "Mercredi";
            case THURSDAY: return "Jeudi";
            case FRIDAY: return "Vendredi";
            case SATURDAY: return "Samedi";
            case SUNDAY: return "Dimanche";
            default: return dayOfWeek.toString();
        }
    }


    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public void removeAssignment(Assignment assignment) {
        assignments.remove(assignment);
    }

}
