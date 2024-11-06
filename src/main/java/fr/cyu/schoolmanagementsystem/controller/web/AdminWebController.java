package fr.cyu.schoolmanagementsystem.controller.web;

import fr.cyu.schoolmanagementsystem.model.dto.CourseDTO;
import fr.cyu.schoolmanagementsystem.model.dto.StudentDTO;
import fr.cyu.schoolmanagementsystem.service.EnrollmentService;
import fr.cyu.schoolmanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminWebController {

    private final StudentService studentService;

    private final EnrollmentService enrollmentService;

    public AdminWebController(StudentService studentService, EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public String showDashboard(Model model) {
        return "admin/dashboard";
    }

    @GetMapping("/students")
    public String getAllStudents(Model model) {
        List<StudentDTO> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "admin/students/list"; // JSP page name: students/list.jsp
    }

    @GetMapping("/students/{id}")
    public String getStudentById(@PathVariable("id") UUID studentId, Model model, RedirectAttributes redirectAttributes) {
        Optional<StudentDTO> student = studentService.getStudentById(studentId);

        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            return "admin/students/view"; // JSP page name: students/view.jsp
        } else {
            redirectAttributes.addFlashAttribute("error", "Student not found");
            return "redirect:/admin/students"; // Redirect to the list page if student not found
        }
    }

    @GetMapping("/students/{id}/courses")
    public String showCoursesForStudent(@PathVariable UUID id, Model model, RedirectAttributes redirectAttributes) {
        List<CourseDTO> courses = enrollmentService.getCoursesForStudent(id);
        Optional<StudentDTO> student = studentService.getStudentById(id);
        if (courses != null && student.isPresent()) {
            model.addAttribute("courses", courses);
            model.addAttribute("student", student.get());
            return "admin/students/courses"; // JSP page name: students/courses.jsp
        } else {
            redirectAttributes.addFlashAttribute("error", "Courses not found for student");
            return "redirect:/admin";
        }
    }

    @GetMapping("/new")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        return "admin/students/form"; // JSP page for the form to add a new student
    }

    @PostMapping("/students")
    public String addStudent(@Valid @ModelAttribute("student") StudentDTO studentDTO,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            UUID newStudentId = studentService.addStudent(studentDTO);
            redirectAttributes.addFlashAttribute("message", "Student created successfully");
            return "redirect:/admin/students/" + newStudentId; // Redirect to the new student's view page
        } catch (RuntimeException ex) {
            model.addAttribute("error", "Conflict: " + ex.getMessage());
            return "admin/students/form"; // Reload the form with an error message
        }
    }

    @DeleteMapping("/students/{id}")
    public String deleteStudentById(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudentById(id);
            redirectAttributes.addFlashAttribute("message", "Student deleted successfully");
            return "redirect:/admin/students"; // Redirect to the list of students after deletion
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", "Conflict: " + ex.getMessage());
            return "redirect:/admin/students"; // Redirect back to the list with an error message
        }
    }
}
