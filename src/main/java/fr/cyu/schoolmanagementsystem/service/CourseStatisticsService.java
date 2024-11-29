package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.model.dto.CourseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

@Service
public class CourseStatisticsService {

    private final CourseService courseService;
    private final GradeService gradeService;

    // Stockage en mémoire des statistiques des cours
    private Map<UUID, Double> courseAverageGrades = new HashMap<>();
    private Map<UUID, Double> courseMinGrades = new HashMap<>();
    private Map<UUID, Double> courseMaxGrades = new HashMap<>();

    @Autowired
    public CourseStatisticsService(CourseService courseService, GradeService gradeService) {
        this.courseService = courseService;
        this.gradeService = gradeService;
    }

    @PostConstruct
    public void initializeCourseStatistics() {
        // Récupérer tous les cours
        List<CourseDTO> courses = courseService.getAllCourses();

        // Calculer les statistiques pour chaque cours et les stocker en mémoire
        for (CourseDTO course : courses) {
            double averageGrade = gradeService.calculateAverageGradeForCourse(course.getId());
            double minGrade = gradeService.getMinAverageForCourse(course.getId());
            double maxGrade = gradeService.getMaxAverageForCourse(course.getId());

            // Stocker les résultats dans la mémoire
            courseAverageGrades.put(course.getId(), averageGrade);
            courseMinGrades.put(course.getId(), minGrade);
            courseMaxGrades.put(course.getId(), maxGrade);

            System.out.println("Course ID: " + course.getId() + ", Average: " + averageGrade + ", Min: " + minGrade + ", Max: " + maxGrade);
        }
    }

    // Méthodes pour récupérer les statistiques en mémoire
    public double getCourseAverage(UUID courseId) {
        return courseAverageGrades.getOrDefault(courseId, 0.0);
    }


    public double getCourseMin(UUID courseId) {
        return courseMinGrades.getOrDefault(courseId, 0.0);
    }

    public double getCourseMax(UUID courseId) {
        return courseMaxGrades.getOrDefault(courseId, 0.0);
    }
}
