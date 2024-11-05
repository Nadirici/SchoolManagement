package fr.cyu.schoolmanagementsystem.controller.web;

import fr.cyu.schoolmanagementsystem.model.dto.GradeDTO;
import fr.cyu.schoolmanagementsystem.service.GradeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/admin/grades")
public class GradeWebController {

    private final GradeService gradeService;

    public GradeWebController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping("/new")
    public String showGradeForm(Model model) {
        model.addAttribute("grade", new GradeDTO());
        return "grades/form"; // Nom de la page JSP pour le formulaire d'attribution de note
    }

    @PostMapping
    public String gradeStudentToCourse(@ModelAttribute("grade") GradeDTO gradeDTO,
                                       RedirectAttributes redirectAttributes) {
        try {
            UUID gradeId = gradeService.gradeStudent(gradeDTO);
            redirectAttributes.addFlashAttribute("message", "Grade assigned successfully");
            return "redirect:/admin/grades"; // Redirection vers une page appropriée après l'attribution de la note
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", "Grade assignment failed: " + ex.getMessage());
            return "redirect:/admin/grades/new"; // Redirection vers le formulaire avec un message d'erreur
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteGrade(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            gradeService.deleteGrade(id);
            redirectAttributes.addFlashAttribute("message", "Grade deleted successfully");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", "Grade deletion failed: " + ex.getMessage());
        }
        return "redirect:/admin/grades"; // Redirection vers la page de gestion des notes
    }
}
