package fr.cyu.schoolmanagementsystem.controller.web;

import fr.cyu.schoolmanagementsystem.model.dto.TeacherDTO;
import fr.cyu.schoolmanagementsystem.service.TeacherService;
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
@RequestMapping("/admin/teachers")
public class TeacherWebController {

    private final TeacherService teacherService;

    @Autowired
    public TeacherWebController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public String getAllTeachers(Model model) {
        List<TeacherDTO> teachers = teacherService.getAllTeachers();
        model.addAttribute("teachers", teachers);
        return "teachers/list"; // Nom de la page JSP pour lister les enseignants
    }

    @GetMapping("/{id}")
    public String getTeacherById(@PathVariable("id") UUID teacherId, Model model, RedirectAttributes redirectAttributes) {
        Optional<TeacherDTO> teacher = teacherService.getTeacherById(teacherId);

        if (teacher.isPresent()) {
            model.addAttribute("teacher", teacher.get());
            return "teachers/view"; // Nom de la page JSP pour afficher un enseignant
        } else {
            redirectAttributes.addFlashAttribute("error", "Teacher not found");
            return "redirect:/admin/teachers"; // Redirige vers la liste des enseignants en cas d'absence
        }
    }

    @GetMapping("/new")
    public String showAddTeacherForm(Model model) {
        model.addAttribute("teacher", new TeacherDTO());
        return "teachers/form"; // Nom de la page JSP pour ajouter un nouvel enseignant
    }

    @PostMapping
    public String addTeacher(@Valid @ModelAttribute("teacher") TeacherDTO teacherDTO,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            UUID newTeacherId = teacherService.addTeacher(teacherDTO);
            redirectAttributes.addFlashAttribute("message", "Teacher created successfully");
            return "redirect:/admin/teachers/" + newTeacherId; // Redirection vers la page de l'enseignant créé
        } catch (RuntimeException ex) {
            model.addAttribute("error", "Conflict: " + ex.getMessage());
            return "teachers/form"; // Recharge le formulaire avec un message d'erreur
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteTeacherById(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            teacherService.deleteTeacherById(id);
            redirectAttributes.addFlashAttribute("message", "Teacher deleted successfully");
            return "redirect:/admin/teachers"; // Redirection vers la liste des enseignants après suppression
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", "Conflict: " + ex.getMessage());
            return "redirect:/admin/teachers"; // Redirection vers la liste avec un message d'erreur
        }
    }
}
