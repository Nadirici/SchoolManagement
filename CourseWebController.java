package fr.cyu.schoolmanagementsystem.controller.web;

import fr.cyu.schoolmanagementsystem.model.dto.CourseDTO;
import fr.cyu.schoolmanagementsystem.model.dto.StudentDTO;
import fr.cyu.schoolmanagementsystem.service.CourseService;
import fr.cyu.schoolmanagementsystem.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin/courses")
public class CourseWebController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @Autowired
    public CourseWebController(CourseService courseService, EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public String getAllCourses(Model model) {
        List<CourseDTO> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "courses/list"; // Nom de la page JSP pour lister les cours
    }

    @GetMapping("/{id}")
    public String getCourseById(@PathVariable("id") UUID courseId, Model model, RedirectAttributes redirectAttributes) {
        Optional<CourseDTO> course = courseService.getCourseById(courseId);

        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            return "courses/view"; // Nom de la page JSP pour afficher un cours
        } else {
            redirectAttributes.addFlashAttribute("error", "Course not found");
            return "redirect:/admin/courses"; // Redirection vers la liste des cours en cas d'absence
        }
    }

    @GetMapping("/new")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new CourseDTO());
        return "courses/form"; // Nom de la page JSP pour ajouter un nouveau cours
    }

    @PostMapping
    public String addCourse(@Valid @ModelAttribute("course") CourseDTO courseDTO,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        try {
            UUID newCourseId = courseService.addCourse(courseDTO);
            redirectAttributes.addFlashAttribute("message", "Course created successfully");
            return "redirect:/admin/courses/" + newCourseId; // Redirection vers la page du cours créé
        } catch (RuntimeException ex) {
            model.addAttribute("error", "Conflict: " + ex.getMessage());
            return "courses/form"; // Recharge le formulaire avec un message d'erreur
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteCourseById(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourseById(id);
            redirectAttributes.addFlashAttribute("message", "Course deleted successfully");
            return "redirect:/admin/courses"; // Redirection vers la liste des cours après suppression
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", "Conflict: " + ex.getMessage());
            return "redirect:/admin/courses"; // Redirection vers la liste avec un message d'erreur
        }
    }

    @GetMapping("/{id}/students")
    public String getStudentsForCourse(@PathVariable UUID id, Model model, RedirectAttributes redirectAttributes) {
        List<StudentDTO> students = enrollmentService.getStudentsForCourse(id);
        if (!students.isEmpty()) {
            model.addAttribute("students", students);
            return "courses/students"; // Nom de la page JSP pour lister les étudiants d'un cours
        } else {
            redirectAttributes.addFlashAttribute("error", "No students enrolled in this course");
            return "redirect:/admin/courses"; // Redirection si aucun étudiant n'est trouvé
        }
    }
}
