package fr.cyu.schoolmanagementsystem.controller.web;

import fr.cyu.schoolmanagementsystem.model.dto.EnrollmentDTO;
import fr.cyu.schoolmanagementsystem.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/enrollments")
public class EnrollmentWebController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentWebController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public String getAllEnrollments(Model model) {
        List<EnrollmentDTO> enrollments = enrollmentService.getAllEnrollments();
        model.addAttribute("enrollments", enrollments);
        return "enrollments/list"; // Nom de la page JSP pour lister les inscriptions
    }

    @GetMapping("/new")
    public String showEnrollmentForm(Model model) {
        model.addAttribute("enrollment", new EnrollmentDTO());
        return "enrollments/form"; // Nom de la page JSP pour ajouter une nouvelle inscription
    }

    @PostMapping
    public String enrollStudentToCourse(@ModelAttribute("enrollment") EnrollmentDTO enrollmentDTO,
                                        RedirectAttributes redirectAttributes) {
        try {
            UUID enrollmentId = enrollmentService.enrollStudent(enrollmentDTO);
            redirectAttributes.addFlashAttribute("message", "Student enrolled successfully");
            return "redirect:/admin/enrollments"; // Redirection vers la liste des inscriptions
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", "Enrollment failed: " + ex.getMessage());
            return "redirect:/admin/enrollments/new"; // Redirection vers le formulaire avec un message d'erreur
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteEnrollment(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            enrollmentService.deleteEnrollment(id);
            redirectAttributes.addFlashAttribute("message", "Enrollment deleted successfully");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", "Deletion failed: " + ex.getMessage());
        }
        return "redirect:/admin/enrollments"; // Redirection vers la liste des inscriptions
    }
}
