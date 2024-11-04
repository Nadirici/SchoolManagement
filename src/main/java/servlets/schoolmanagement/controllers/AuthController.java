package servlets.schoolmanagement.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import servlets.schoolmanagement.dto.UserRegistrationDto;
import servlets.schoolmanagement.models.Enumerations.Departement;
import servlets.schoolmanagement.models.entity.Admin;
import servlets.schoolmanagement.models.entity.RegistrationRequest;
import servlets.schoolmanagement.models.entity.Student;
import servlets.schoolmanagement.models.entity.Teacher;
import servlets.schoolmanagement.models.passwordManager.HashPassword;
import servlets.schoolmanagement.services.RequestService;
import servlets.schoolmanagement.services.StudentService;
import servlets.schoolmanagement.services.TeacherService;

import java.sql.Date;
import java.util.Base64;

@Controller
public class AuthController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final RequestService requestService;

    public AuthController(StudentService studentService, TeacherService teacherService, RequestService requestService) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.requestService = requestService;
    }

    @GetMapping("/auth")
    public String showAuthPage() {
        return "index";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserRegistrationDto userDto, RedirectAttributes redirectAttributes) {
        if (studentService.userExists(userDto.getEmail(), userDto.getLastname(), userDto.getFirstname()) ||
                teacherService.userExistsByEmail(userDto.getEmail())) {
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

        // Vérifier si l'utilisateur est un étudiant
        Student student = studentService.findStudentByEmail(email);
        if (student != null && HashPassword.verifyPassword(password, student.getPassword(), student.getSalt())) {
            if (student.isVerified()) {
                session.setAttribute("userId", student.getId());
                return "redirect:/student/dashboard/" + student.getId();
            } else {
                redirectAttributes.addFlashAttribute("flashError", "Votre compte étudiant n'est pas encore vérifié.");
                return "redirect:/auth";
            }
        }

        // Vérifier si l'utilisateur est un enseignant
        Teacher teacher = teacherService.findTeacherByEmail(email);
        if (teacher != null && HashPassword.verifyPassword(password, teacher.getPassword(), teacher.getSalt())) {
            if (teacher.isVerified()) {
                session.setAttribute("userId", teacher.getId());
                return "redirect:/teacher/dashboard/" + teacher.getId();
            } else {
                redirectAttributes.addFlashAttribute("flashError", "Votre compte enseignant n'est pas encore vérifié.");
                return "redirect:/auth";
            }
        }

        // Vérifier si l'utilisateur est un administrateur
        if (email.equals(Admin.getAdmin().getEmail()) && password.equals(Admin.getAdmin().getPassword())) {
            session.setAttribute("userId", Admin.getAdmin().getId());
            return "redirect:/admin/dashboard/" + Admin.getAdmin().getId() + "/requests";
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
