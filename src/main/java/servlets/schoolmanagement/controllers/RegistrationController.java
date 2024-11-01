package servlets.schoolmanagement.controllers;

import servlets.schoolmanagement.dto.UserRegistrationDto;
import servlets.schoolmanagement.models.Enumerations.Departement;
import servlets.schoolmanagement.models.entity.RegistrationRequest;
import servlets.schoolmanagement.models.entity.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import servlets.schoolmanagement.models.entity.Teacher;
import servlets.schoolmanagement.services.RequestService; // Importez votre service de demande
import servlets.schoolmanagement.services.StudentService;
import servlets.schoolmanagement.services.TeacherService;

import java.sql.Date;

@Controller
public class RegistrationController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final RequestService requestService; // Ajout du service RequestService


    public RegistrationController(StudentService studentService, TeacherService teacherService, RequestService requestService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.requestService = requestService;
    }

    @GetMapping
    public String home() {
        return "redirect:/auth";
    }

    @GetMapping("/auth")
    public String showAuthPage() {
        return "index";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserRegistrationDto userDto, RedirectAttributes redirectAttributes) {

        // Vérification de l'existence de l'utilisateur
        if (studentService.userExists(userDto.getEmail(), userDto.getLastname(), userDto.getFirstname())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Un utilisateur avec ces informations existe déjà.");
            return "redirect:/"; // Redirige vers la page d'accueil avec un message d'erreur
        }

        try {
            if ("student".equalsIgnoreCase(userDto.getUserType())) {
                // Conversion de la date de naissance
                Date dateOfBirth = Date.valueOf(userDto.getDateOfBirth());

                //Creation of a new student
                Student student = new Student(
                        userDto.getFirstname(),
                        userDto.getLastname(),
                        dateOfBirth,
                        userDto.getEmail(),
                        userDto.getPassword()
                );
                studentService.registerStudent(student);

                RegistrationRequest request = new RegistrationRequest(student);// Crée une demande
                requestService.saveRequest(request);

                redirectAttributes.addFlashAttribute("message", "Demande d'inscription d'étudiant soumise avec succès !");

            } else if ("teacher".equalsIgnoreCase(userDto.getUserType())) {
                Teacher teacher= new Teacher(
                        userDto.getFirstname(),
                        userDto.getLastname(),
                        userDto.getEmail(),
                        userDto.getPassword(),
                        Departement.fromDisplayName(userDto.getDepartment())
                );
                teacherService.registerTeacher(teacher);

                RegistrationRequest request = new RegistrationRequest(teacher);// Crée une demande
                requestService.saveRequest(request);

                redirectAttributes.addFlashAttribute("message", "Demande d'inscription de professeur soumise avec succès !");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Type d'utilisateur invalide.");
                return "redirect:/"; // Redirige vers la page d'accueil avec un message d'erreur
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la soumission de la demande : " + e.getMessage());
            return "redirect:/"; // Redirige vers la page d'accueil avec un message d'erreur
        }

        return "redirect:/"; // Redirige vers la page d'accueil après une demande réussie
    }
}
