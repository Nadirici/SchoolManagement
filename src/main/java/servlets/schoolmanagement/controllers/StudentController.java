package servlets.schoolmanagement.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import servlets.schoolmanagement.models.entity.Student;
import servlets.schoolmanagement.services.StudentService;

import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/dashboard/{id}")
    public String showStudentDashboard(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Student> studentOptional = studentService.findStudentById(id);

        if (studentOptional.isEmpty() || !studentOptional.get().isVerified()) {
            redirectAttributes.addFlashAttribute("flashError", "Accès refusé : compte non vérifié ou élève introuvable.");
            return "redirect:/noaccess"; // Utiliser redirection absolue
        }

        Student student = studentOptional.get();
        model.addAttribute("student", student);
        return "studentDashboard";
    }
}
