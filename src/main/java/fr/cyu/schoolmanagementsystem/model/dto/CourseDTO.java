package fr.cyu.schoolmanagementsystem.model.dto;

import fr.cyu.schoolmanagementsystem.model.entity.Assignment;
import fr.cyu.schoolmanagementsystem.model.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.model.entity.Teacher;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Getter @Setter
public class CourseDTO {

    private UUID id;


    private String name;

    private String description;


    private Teacher teacher;


    private Set<Enrollment> enrollments;


    private Set<Assignment> assignments;


    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

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


}
