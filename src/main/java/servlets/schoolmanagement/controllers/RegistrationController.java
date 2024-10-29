package servlets.schoolmanagement.controllers;

import servlets.schoolmanagement.models.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import servlets.schoolmanagement.services.StudentService;

import java.sql.Date;

@Controller
@RequestMapping("/api")
public class RegistrationController {

    private final StudentService studentService;

    public RegistrationController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String home(Model model) {
        return "index"; // Renvoie à la page d'accueil
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String userType,
            @RequestParam String email,
            @RequestParam String lastname,
            @RequestParam String firstname,
            @RequestParam String date_of_birth,
            @RequestParam String password,
            RedirectAttributes redirectAttributes) {

        // Vérification de l'existence de l'utilisateur
        if (studentService.userExists(email, lastname, firstname)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Un utilisateur avec ces informations existe déjà.");
            return "redirect:/api/"; // Redirige vers la page d'accueil avec un message d'erreur
        }

        try {
            // Conversion de la date de naissance
            Date dateOfBirth = Date.valueOf(date_of_birth);

            if ("student".equalsIgnoreCase(userType)) {
                // Création de l'objet Student et sauvegarde
                Student student = new Student(firstname, lastname, dateOfBirth, email, password);
                studentService.registerStudent(student);
                redirectAttributes.addFlashAttribute("message", "Inscription réussie !");
            } else if ("teacher".equalsIgnoreCase(userType)) {
                // Logique pour enregistrer un professeur
                redirectAttributes.addFlashAttribute("message", "Inscription du professeur réussie !");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Type d'utilisateur invalide.");
                return "redirect:/api/"; // Redirige vers la page d'accueil avec un message d'erreur
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'inscription : " + e.getMessage());
            return "redirect:/api/"; // Redirige vers la page d'accueil avec un message d'erreur
        }

        return "redirect:/api/"; // Redirige vers la page d'accueil après une inscription réussie
    }
}
