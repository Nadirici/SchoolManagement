package fr.cyu.schoolmanagementsystem.controller.web;

import fr.cyu.schoolmanagementsystem.model.dto.CourseDTO;
import fr.cyu.schoolmanagementsystem.model.dto.TeacherDTO;
import fr.cyu.schoolmanagementsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/teachers")
public class TeacherWebController {

    private final TeacherService teacherService;
    private final EnrollmentService enrollmentService;
    private final CourseService courseService;
    private final GradeService gradeService;
    private final AssignmentService assignmentService;

    @Autowired
    public TeacherWebController(TeacherService teacherService, EnrollmentService enrollmentService, CourseService courseService, GradeService gradeService, AssignmentService assignmentService) {
        this.teacherService = teacherService;
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
        this.gradeService = gradeService;
        this.assignmentService = assignmentService;
    }

    @GetMapping("/{id}")
    public String showTeacherDashboard(@PathVariable("id") UUID teacherId, Model model, RedirectAttributes redirectAttributes) {
        Optional<TeacherDTO> teacher = teacherService.getTeacherById(teacherId);

        if (teacher.isPresent()) {
            List<CourseDTO> courses = courseService.getCoursesByTeacherId(teacherId);
            Map<UUID, Double> courseAverages = new HashMap<>();
            Map<UUID, Double> courseMinAverages = new HashMap<>();
            Map<UUID, Double> courseMaxAverages = new HashMap<>();
            for (CourseDTO course : courses) {
                double averageGrade = gradeService.calculateAverageGradeForCourse(course.getId());
                double minAverageGrade = gradeService.getMinAverageForCourse(course.getId());
                double maxAverageGrade = gradeService.getMaxAverageForCourse(course.getId());
                courseAverages.put(course.getId(), averageGrade);
                courseMinAverages.put(course.getId(), minAverageGrade);
                courseMaxAverages.put(course.getId(), maxAverageGrade);
            }
            model.addAttribute("teacher", teacher.get());
            model.addAttribute("courses", courses);
            model.addAttribute("courseAverages", courseAverages);
            model.addAttribute("minAverages", courseMinAverages);
            model.addAttribute("maxAverages", courseMaxAverages);
            return "teachers/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "teacher not found");
            return "redirect:/teachers"; // Redirect to the list page if student not found
        }
    }
}
