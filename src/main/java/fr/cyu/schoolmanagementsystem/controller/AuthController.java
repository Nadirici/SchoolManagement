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

            return "redirect:/auth?flashMessage=emailAlreadyExists";
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
                return "redirect:/auth?flashMessage=studentRequestSubmitted";
            } else if ("teacher".equalsIgnoreCase(userDto.getUserType())) {
                String salt = Base64.getEncoder().encodeToString(HashPassword.generateSalt());
                String hashedPassword = HashPassword.hashPassword(userDto.getPassword(), Base64.getDecoder().decode(salt));
                Teacher teacher = new Teacher(userDto.getFirstname(), userDto.getLastname(),
                        userDto.getEmail(), hashedPassword, Departement.fromDisplayName(userDto.getDepartment()), salt);
                teacherService.registerTeacher(teacher);
                // Crée une demande
                RegistrationRequest request = new RegistrationRequest(teacher);
                requestService.saveRequest(request);

                return "redirect:/auth?flashMessage=teacherRequestSubmitted";
            } else {
                return "redirect:/auth?flashMessage=invalidUserType";
            }
        } catch (Exception e) {
            return "redirect:/auth?flashMessage=generalError";
        }

    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Assurez-vous que vous avez une page login.jsp ou login.html
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        RedirectAttributes redirectAttributes, HttpSession session) {

        try {
            Optional<Student> studentOptional = studentService.getStudentByEmail(email);

            if (studentOptional.isPresent()) {
                Student student = studentOptional.get();
                if (HashPassword.verifyPassword(password, student.getPassword(), student.getSalt())) {
                    if (student.isVerified()) {
                        session.setAttribute("userId", student.getId());
                        session.setAttribute("isAuthenticated", true);
                        session.setAttribute("userType", "student");
                        return "redirect:/students/" + student.getId();
                    } else {
                        return "redirect:/auth?flashMessage=unVerifiedStudent";
                    }
                } else {
                    return "redirect:/auth?flashMessage=incorrectPassword";
                }
            }

            Optional<Teacher> teacherOptional = teacherService.getTeacherByEmail(email);

            if (teacherOptional.isPresent()) {
                Teacher teacher = teacherOptional.get();
                if (HashPassword.verifyPassword(password, teacher.getPassword(), teacher.getSalt())) {
                    if (teacher.isVerified()) {
                        session.setAttribute("userId", teacher.getId());
                        session.setAttribute("isAuthenticated", true);
                        session.setAttribute("userType", "teacher");
                        return "redirect:/teachers/" + teacher.getId();
                    } else {
                        return "redirect:/auth?flashMessage=unVerifiedTeacher";
                    }
                } else {
                    return "redirect:/auth?flashMessage=incorrectPassword";
                }
            }

            Admin optionalAdmin = adminService.getAdminByEmail(email);

            if (optionalAdmin != null) {
                if (HashPassword.verifyPassword(password, optionalAdmin.getPassword(), optionalAdmin.getSalt())) {
                    session.setAttribute("userId", optionalAdmin.getId());
                    session.setAttribute("userType", "admin");
                    session.setAttribute("isAuthenticated", true);
                    return "redirect:/admin/" + optionalAdmin.getId();
                } else {
                    return "redirect:/auth?flashMessage=incorrectEmailOrPassword";
                }
            }

            // Si aucun utilisateur n'a été trouvé avec cet email
            return "redirect:/auth?flashMessage=incorrectEmail";

        } catch (RuntimeException e) {
            return "redirect:/auth?flashMessage=generalError";
        }
    }


    @RequestMapping("/noaccess")
    public String showNoAccesPage() {
        return "noaccess";
    }


    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("flashSuccess", "Vous avez été déconnecté avec succès.");
        return "redirect:/auth";
    }
}