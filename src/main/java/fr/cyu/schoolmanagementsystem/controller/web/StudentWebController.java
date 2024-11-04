package fr.cyu.schoolmanagementsystem.controller.web;

import fr.cyu.schoolmanagementsystem.model.dto.CourseDTO;
import fr.cyu.schoolmanagementsystem.model.dto.StudentDTO;
import fr.cyu.schoolmanagementsystem.model.entity.Student;
import fr.cyu.schoolmanagementsystem.service.EnrollmentService;
import fr.cyu.schoolmanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/admin/students")
public class StudentWebController {

    private final StudentService studentService;
    private final EnrollmentService enrollmentService;

    @Autowired
    public StudentWebController(StudentService studentService, EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public String getAllStudents(Model model) {
        List<StudentDTO> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "students/list"; // JSP page name: students/list.jsp
    }

    @GetMapping("/{id}")
    public String getStudentById(@PathVariable("id") UUID studentId, Model model, RedirectAttributes redirectAttributes) {
        Optional<StudentDTO> student = studentService.getStudentById(studentId);

        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            return "students/view"; // JSP page name: students/view.jsp
        } else {
            redirectAttributes.addFlashAttribute("error", "Student not found");
            return "redirect:/admin/students"; // Redirect to the list page if student not found
        }
    }

    @GetMapping("/new")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        return "students/form"; // JSP page for the form to add a new student
    }

    @PostMapping
    public String addStudent(@Valid @ModelAttribute("student") StudentDTO studentDTO,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            UUID newStudentId = studentService.addStudent(studentDTO);
            redirectAttributes.addFlashAttribute("message", "Student created successfully");
            return "redirect:/admin/students/" + newStudentId; // Redirect to the new student's view page
        } catch (RuntimeException ex) {
            model.addAttribute("error", "Conflict: " + ex.getMessage());
            return "students/form"; // Reload the form with an error message
        }
    }

    @DeleteMapping("/{id}")
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

    @GetMapping("/{id}/courses")
    public String getCoursesForStudent(@PathVariable UUID id, Model model, RedirectAttributes redirectAttributes) {
        List<CourseDTO> courses = enrollmentService.getCoursesForStudent(id);
        Optional<StudentDTO> student = studentService.getStudentById(id);
        if (courses != null && student.isPresent()) {
            model.addAttribute("courses", courses);
            model.addAttribute("student", student.get());
            return "students/courses"; // JSP page name: students/courses.jsp
        } else {
            redirectAttributes.addFlashAttribute("error", "Courses not found for student");
            return "redirect:/admin/students";
        }
    }
}
