package servlets.schoolmanagement.controllers;

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
@RequestMapping("/api")
public class RegistrationController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final RequestService requestService; // Ajout du service RequestService


    public RegistrationController(StudentService studentService, TeacherService teacherService, RequestService requestService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.requestService = requestService;
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
            @RequestParam(required = false) String date_of_birth,
            @RequestParam String password,
            @RequestParam(required = false) String department,
            RedirectAttributes redirectAttributes) {

        // Vérification de l'existence de l'utilisateur
        if (studentService.userExists(email, lastname, firstname)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Un utilisateur avec ces informations existe déjà.");
            return "redirect:/api/"; // Redirige vers la page d'accueil avec un message d'erreur
        }

        try {
            if ("student".equalsIgnoreCase(userType)) {
                // Conversion de la date de naissance
                Date dateOfBirth = Date.valueOf(date_of_birth);

                //Creation of a new student
                Student student = new Student(firstname, lastname, dateOfBirth, email, password);


                // isVerified = 0
                studentService.registerStudent(student);


                RegistrationRequest request = new RegistrationRequest(student);// Crée une demande



                requestService.saveRequest(request);
                redirectAttributes.addFlashAttribute("message", "Demande d'inscription d'étudiant soumise avec succès !");

            } else if ("teacher".equalsIgnoreCase(userType)) {

                Teacher teacher= new Teacher(firstname, lastname, email, password,Departement.fromDisplayName(department) );


                // isVerified = 0
                teacherService.registerTeacher(teacher);


                RegistrationRequest request = new RegistrationRequest(teacher);// Crée une demande



                requestService.saveRequest(request);

               // requestService.saveRequest();
                redirectAttributes.addFlashAttribute("message", "Demande d'inscription de professeur soumise avec succès !");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Type d'utilisateur invalide.");
                return "redirect:/api/"; // Redirige vers la page d'accueil avec un message d'erreur
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la soumission de la demande : " + e.getMessage());
            return "redirect:/api/"; // Redirige vers la page d'accueil avec un message d'erreur
        }

        return "redirect:/api/"; // Redirige vers la page d'accueil après une demande réussie
    }
}
