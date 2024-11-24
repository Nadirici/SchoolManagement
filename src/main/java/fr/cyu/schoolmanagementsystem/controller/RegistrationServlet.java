package fr.cyu.schoolmanagementsystem.controller;

import fr.cyu.schoolmanagementsystem.dao.*;
import fr.cyu.schoolmanagementsystem.entity.*;
import fr.cyu.schoolmanagementsystem.service.*;
import fr.cyu.schoolmanagementsystem.util.HashPassword;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final RequestService requestService;

    // Constructeur pour initialiser les services nécessaires
    public RegistrationServlet() {
        studentService = new StudentService(new StudentDAO(Student.class));
        teacherService = new TeacherService(new TeacherDAO(Teacher.class));
        requestService = new RequestService(new RegistrationRequestDAO(RegistrationRequest.class));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String dateOfBirthStr = request.getParameter("dateOfBirth"); // Format: YYYY-MM-DD
        String department = request.getParameter("department");

        Student studentOptional = null;
        Teacher teacherOptional = null;

        try {
            studentOptional = studentService.getByEmail(email);
        } catch (Exception _) {

        }

        try {
            teacherOptional = teacherService.getByEmail(email);
        } catch (Exception _) {

        }

        if ("student".equalsIgnoreCase(userType)) {

            if (studentOptional != null || teacherOptional != null) {
                response.sendRedirect(request.getContextPath() + "/login?flashMessage=emailAlreadyExists");
                return;
            }

            // Enregistrer l'étudiant
            String salt = Base64.getEncoder().encodeToString(HashPassword.generateSalt());
            String hashedPassword = HashPassword.hashPassword(password, Base64.getDecoder().decode(salt));
            LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStr);
            Student student = new Student(firstname, lastname, dateOfBirth, email, hashedPassword, salt, false);
            UUID id = studentService.add(student);

            // Créer et enregistrer la demande d'inscription
            RegistrationRequest registrationRequest = new RegistrationRequest(student);
            requestService.add(registrationRequest);

            response.sendRedirect(request.getContextPath() + "/login?flashMessage=studentRequestSubmitted");
            return;
        }

        if ("teacher".equalsIgnoreCase(userType)) {

            if (teacherOptional != null || studentOptional != null) {
                response.sendRedirect(request.getContextPath() + "/login?flashMessage=emailAlreadyExists");
                return;
            }

            // Enregistrer l'enseignant
            String salt = Base64.getEncoder().encodeToString(HashPassword.generateSalt());
            String hashedPassword = HashPassword.hashPassword(password, Base64.getDecoder().decode(salt));

            System.out.println(email);

            if (email == null || email.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/login?flashMessage=emptyEmail");

                return;
            }

            Teacher teacher = new Teacher(firstname, lastname, email, hashedPassword, department, salt, false);
            teacherService.add(teacher);

            // Créer et enregistrer la demande d'inscription
            RegistrationRequest registrationRequest = new RegistrationRequest(teacher);
            requestService.add(registrationRequest);

            response.sendRedirect(request.getContextPath() + "/login?flashMessage=teacherRequestSubmitted");


            return;
        }

        // Si le type d'utilisateur est invalide

        response.sendRedirect(request.getContextPath() + "/login?flashMessage=invalidUserType");
    }
}
