package servlets.schoolmanagement.controllers;
import org.springframework.lang.NonNull;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import servlets.schoolmanagement.dto.UserRegistrationDto;
import servlets.schoolmanagement.models.Enumerations.Departement;
import servlets.schoolmanagement.models.entity.Admin;
import servlets.schoolmanagement.models.entity.RegistrationRequest;
import servlets.schoolmanagement.models.entity.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import servlets.schoolmanagement.models.entity.Teacher;
import servlets.schoolmanagement.models.passwordManager.HashPassword;
import servlets.schoolmanagement.services.RequestService; // Importez votre service de demande
import servlets.schoolmanagement.services.StudentService;
import servlets.schoolmanagement.services.TeacherService;
import java.sql.Date;
import java.util.Base64;
import java.util.Optional;


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

    @GetMapping("/noaccess")
    public String noaccess(){
        return "noaccess";
    }

    @GetMapping("/teacherDashboard/{id}")
    public String showTeacherDashboard(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        System.out.println("Recherche de l'enseignant avec ID : " + id);

        Optional<Teacher> teacherOptional = teacherService.findTeacherById(id);
        if (teacherOptional.isPresent()) {
            Teacher teacher = teacherOptional.get();
            System.out.println("Enseignant trouvé : " + teacher.getFirstName() + " " + teacher.getLastName());
        } else {
            System.out.println("Aucun enseignant trouvé avec l'ID : " + id);
        }

        // Vérifier si l'enseignant existe et est vérifié
        if (teacherOptional.isEmpty() || !teacherOptional.get().isVerified()) {
            redirectAttributes.addFlashAttribute("flashError", "Accès refusé :  Enseignant introuvable.");
            return "redirect:/noaccess"; // Utiliser redirection absolue

        }

        // Ajouter l'enseignant en attribut pour la vue
        Teacher teacher = teacherOptional.get();
        model.addAttribute("teacher", teacher);

        // Rediriger vers la page du tableau de bord de l'enseignant
        return "teacherDashboard";
    }


    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // Invalidating the session to log out the user
        session.invalidate();
        redirectAttributes.addFlashAttribute("flashSuccess", "Vous avez été déconnecté avec succès.");
        return "redirect:/auth"; // Redirige vers la page d'authentification
    }


    @GetMapping("/studentDashboard/{id}")
    public String showStudentDashboard(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Student> studentOptional = studentService.findStudentById(id);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            System.out.println("Enseignant trouvé : " + student.getFirstName() + " " + student.getLastName());
        } else {
            System.out.println("Aucun enseignant trouvé avec l'ID : " + id);
        }

        if (studentOptional.isEmpty() || !studentOptional.get().isVerified()) {
            redirectAttributes.addFlashAttribute("flashError", "Accès refusé : compte non vérifié ou élève introuvable.");
            return "redirect:/noaccess"; // Utiliser redirection absolue
        }


        // Vérifiez si l'ID de l'étudiant commence par "1"
        Student student = studentOptional.get();
        model.addAttribute("student", student);



        return "studentDashboard";
    }



    @PostMapping("/register")
    public String register(@ModelAttribute UserRegistrationDto userDto, RedirectAttributes redirectAttributes) {
        System.out.println("Date de naissance : " + userDto.getDateOfBirth());
        // Vérification de l'existence de l'utilisateur
        if (studentService.userExists(userDto.getEmail(), userDto.getLastname(), userDto.getFirstname()) ||
                teacherService.userExistsByEmail(userDto.getEmail())) {
            redirectAttributes.addFlashAttribute("flashError", "Un utilisateur avec cet e-mail existe déjà.");
            return "redirect:/auth"; // Redirige vers la page d'accueil avec un message d'erreur
        }
        try {

            if ("student".equalsIgnoreCase(userDto.getUserType())) {

                String salt = Base64.getEncoder().encodeToString(HashPassword.generateSalt());
                String hashedPassword = HashPassword.hashPassword(userDto.getPassword(),Base64.getDecoder().decode(salt));
                Date dateOfBirth = Date.valueOf(userDto.getDateOfBirth());
                //Creation of a new student
                Student student = new Student(
                        userDto.getFirstname(),
                        userDto.getLastname(),
                        dateOfBirth.toLocalDate(),
                        userDto.getEmail(),
                        hashedPassword,
                        salt
                );
                studentService.registerStudent(student);
                RegistrationRequest request = new RegistrationRequest(student);// Crée une demande
                requestService.saveRequest(request);
                redirectAttributes.addFlashAttribute("flashSuccess", "Demande d'inscription d'étudiant soumise avec succès !");
            } else if ("teacher".equalsIgnoreCase(userDto.getUserType())) {
                String salt = Base64.getEncoder().encodeToString(HashPassword.generateSalt());
                String hashedPassword = HashPassword.hashPassword(userDto.getPassword(),Base64.getDecoder().decode(salt));
                Teacher teacher= new Teacher(
                        userDto.getFirstname(),
                        userDto.getLastname(),
                        userDto.getEmail(),
                        hashedPassword,
                        Departement.fromDisplayName(userDto.getDepartment()),
                        salt
                );
                teacherService.registerTeacher(teacher);
                RegistrationRequest request = new RegistrationRequest(teacher);// Crée une demande
                requestService.saveRequest(request);
                redirectAttributes.addFlashAttribute("flashSuccess", "Demande d'inscription de professeur soumise avec succès !");
            } else {
                redirectAttributes.addFlashAttribute("flashError", "Type d'utilisateur invalide.");
                return "redirect:/auth"; // Redirige vers la page d'accueil avec un message d'erreur
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("flashError", "Erreur lors de la soumission de la demande : " + e.getMessage());
            return "redirect:/auth"; // Redirige vers la page d'accueil avec un message d'erreur
        }
        return "redirect:/auth"; // Redirige vers la page d'accueil après une demande réussie
    }

    @PostMapping("/login")
    public String login(@NonNull @RequestParam("email") String email,
                        @NonNull @RequestParam("password") String password,
                        RedirectAttributes redirectAttributes) {

        // Vérifier si l'utilisateur est un étudiant
        Student student = studentService.findStudentByEmail(email);
        if (student != null && HashPassword.verifyPassword(password, student.getPassword(), student.getSalt())) {
            // Vérifie si l'étudiant est vérifié
            if (student.isVerified()) {

                return "redirect:/studentDashboard/"+student.getId();
            } else {
                redirectAttributes.addFlashAttribute("flashError", "Votre compte étudiant n'est pas encore vérifié.");
                return "redirect:/auth";
            }
        }

        // Vérifier si l'utilisateur est un enseignant
        Teacher teacher = teacherService.findTeacherByEmail(email);
        if (teacher != null && HashPassword.verifyPassword(password, teacher.getPassword(), teacher.getSalt())) {
            // Vérifie si l'enseignant est vérifié
            if (teacher.isVerified()) {

                return "redirect:/teacherDashboard/"+teacher.getId();
            } else {
                redirectAttributes.addFlashAttribute("flashError", "Votre compte enseignant n'est pas encore vérifié.");
                return "redirect:/auth";
            }
        }

        if(email.equals(Admin.getAdmin().getEmail()) && password.equals(Admin.getAdmin().getPassword())) {
            return "redirect:/adminDashboard/"+Admin.getAdmin().getId()+"/requests";
        }

        // Si aucun utilisateur trouvé, ajouter un message d'erreur et rediriger vers la page de connexion
        redirectAttributes.addFlashAttribute("flashError", "Email ou mot de passe incorrect.");
        return "redirect:/auth";
    }



}