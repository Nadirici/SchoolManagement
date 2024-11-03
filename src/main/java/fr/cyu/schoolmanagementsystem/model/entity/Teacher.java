package fr.cyu.schoolmanagementsystem.model.entity;

import fr.cyu.schoolmanagementsystem.model.entity.base.BasePersonEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name = "teachers")
public class Teacher extends BasePersonEntity {

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private Set<Course> courses;

    public Teacher() {
        this.courses = new HashSet<>();
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
    }

}
