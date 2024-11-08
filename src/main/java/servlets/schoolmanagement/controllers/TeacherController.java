package servlets.schoolmanagement.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import servlets.schoolmanagement.models.entity.Teacher;
import servlets.schoolmanagement.services.TeacherService;

import java.util.Optional;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/dashboard/{id}")
    public String showTeacherDashboard(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Teacher> teacherOptional = teacherService.findTeacherById(id);

        if (teacherOptional.isEmpty() || !teacherOptional.get().isVerified()) {
            redirectAttributes.addFlashAttribute("flashError", "Accès refusé : enseignant introuvable.");
            return "redirect:/noaccess"; // Utiliser redirection absolue
        }

        Teacher teacher = teacherOptional.get();
        model.addAttribute("teacher", teacher);
        return "teacherDashboard";

    }
}
