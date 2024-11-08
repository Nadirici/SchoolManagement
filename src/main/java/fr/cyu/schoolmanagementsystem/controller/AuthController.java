package fr.cyu.schoolmanagementsystem.controller;

import fr.cyu.schoolmanagementsystem.model.dto.UserRegistrationDTO;
import fr.cyu.schoolmanagementsystem.model.entity.Admin;
import fr.cyu.schoolmanagementsystem.model.entity.RegistrationRequest;
import fr.cyu.schoolmanagementsystem.model.entity.Student;
import fr.cyu.schoolmanagementsystem.model.entity.Teacher;
import fr.cyu.schoolmanagementsystem.model.entity.enumeration.Departement;
import fr.cyu.schoolmanagementsystem.model.passwordmanager.HashPassword;
import fr.cyu.schoolmanagementsystem.service.AdminService;
import fr.cyu.schoolmanagementsystem.service.RequestService;
import fr.cyu.schoolmanagementsystem.service.StudentService;
import fr.cyu.schoolmanagementsystem.service.TeacherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Controller
public class AuthController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final RequestService requestService;
    private final AdminService adminService;

    public AuthController(StudentService studentService, TeacherService teacherService, RequestService requestService, AdminService adminService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.requestService = requestService;
        this.adminService = adminService;
    }

    @GetMapping("/auth")
    public String showAuthPage() {
        return "index";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserRegistrationDTO userDto, RedirectAttributes redirectAttributes) {
        if (studentService.getStudentByEmail(userDto.getEmail()).isPresent() || teacherService.getTeacherByEmail(userDto.getEmail()).isPresent()) {
            redirectAttributes.addFlashAttribute("flashError", "Un utilisateur avec cet e-mail existe déjà.");
            return "redirect:/auth";
        }
        try {
            if ("student".equalsIgnoreCase(userDto.getUserType())) {
                String salt = Base64.getEncoder().encodeToString(HashPassword.generateSalt());
                String hashedPassword = HashPassword.hashPassword(userDto.getPassword(), Base64.getDecoder().decode(salt));
                Date dateOfBirth = Date.valueOf(userDto.getDateOfBirth());
                Student student = new Student(userDto.getFirstname(), userDto.getLastname(), dateOfBirth.toLocalDate(),
                        userDto.getEmail(), hashedPassword, salt);
                studentService.registerStudent(student);
                RegistrationRequest request = new RegistrationRequest(student);
                requestService.saveRequest(request);
                // Crée une demande
                redirectAttributes.addFlashAttribute("flashSuccess", "Demande d'inscription d'étudiant soumise avec succès !");
            } else if ("teacher".equalsIgnoreCase(userDto.getUserType())) {
                String salt = Base64.getEncoder().encodeToString(HashPassword.generateSalt());
                String hashedPassword = HashPassword.hashPassword(userDto.getPassword(), Base64.getDecoder().decode(salt));
                Teacher teacher = new Teacher(userDto.getFirstname(), userDto.getLastname(),
                        userDto.getEmail(), hashedPassword, Departement.fromDisplayName(userDto.getDepartment()), salt);
                teacherService.registerTeacher(teacher);
                // Crée une demande
                RegistrationRequest request = new RegistrationRequest(teacher);
                requestService.saveRequest(request);
                redirectAttributes.addFlashAttribute("flashSuccess", "Demande d'inscription de professeur soumise avec succès !");
            } else {
                redirectAttributes.addFlashAttribute("flashError", "Type d'utilisateur invalide.");
                return "redirect:/auth";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flashError", "Erreur lors de la soumission de la demande : " + e.getMessage());
            return "redirect:/auth";
        }
        return "redirect:/auth";
    }

    @PostMapping("/login")
    public String login(@NonNull @RequestParam("email") String email,
                        @NonNull @RequestParam("password") String password,
                        RedirectAttributes redirectAttributes, HttpSession session) {

        Optional<Student> studentOptional = studentService.getStudentByEmail(email);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            if (HashPassword.verifyPassword(password, student.getPassword(), student.getSalt())) {
                if (student.isVerified()) {
                    // Stockage de l'ID dans la session
                    // TODO: Check the ID is in every session
                    session.setAttribute("userId", student.getId());
                    return "redirect:/students/" + student.getId();
                } else {
                    redirectAttributes.addFlashAttribute("flashError", "Votre compte étudiant n'est pas encore vérifié.");
                    return "redirect:/auth";
                }
            } else {
                redirectAttributes.addFlashAttribute("flashError", "Mot de passe incorrect");
                return "redirect:/auth";
            }
        } else {
            redirectAttributes.addFlashAttribute("flashError", "Email incorrect");
        }

        Optional<Teacher> teacherOptional = teacherService.getTeacherByEmail(email);

        if (teacherOptional.isPresent()) {
            Teacher teacher = teacherOptional.get();
            if (HashPassword.verifyPassword(password, teacher.getPassword(), teacher.getSalt())) {
                if (teacher.isVerified()) {
                    session.setAttribute("userId", teacher.getId());
                    // Stockage de l'ID dans la session
                    // TODO: Check the ID is in every session
                    return "redirect:/teachers/" + teacher.getId();
                } else {
                    redirectAttributes.addFlashAttribute("flashError", "Votre compte enseignant n'est pas vérifié.");
                    return "redirect:/auth";
                }
            } else {
                redirectAttributes.addFlashAttribute("flashError", "Mot de passe incorrect");
                return "redirect:/auth";
            }
        } else {
            redirectAttributes.addFlashAttribute("flashError", "Email incorrect");
        }


        //UUID uuid = UUID.fromString("e2437152-2648-41ac-bdd6-5f6f273839b0");


        Admin optionalAdmin = adminService.getAdminByEmail(email);






        // Vérifier si l'utilisateur est un administrateur
        if (email.equals(optionalAdmin.getEmail()) && HashPassword.verifyPassword(password, optionalAdmin.getPassword(), optionalAdmin.getSalt())) {
            session.setAttribute("userId", optionalAdmin.getId());
            // Stockage de l'ID dans la session
            // TODO: Check the ID is in every session
            return "redirect:/admin/"+optionalAdmin.getId();
        }

        // Si aucun utilisateur trouvé, ajouter un message d'erreur et rediriger vers la page de connexion
        redirectAttributes.addFlashAttribute("flashError", "Email ou mot de passe incorrect.");
        return "redirect:/auth";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("flashSuccess", "Vous avez été déconnecté avec succès.");
        return "redirect:/auth";
    }
}