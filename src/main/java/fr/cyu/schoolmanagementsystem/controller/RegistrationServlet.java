package fr.cyu.schoolmanagementsystem.controller;

import fr.cyu.schoolmanagementsystem.dao.*;
import fr.cyu.schoolmanagementsystem.entity.*;
import fr.cyu.schoolmanagementsystem.service.*;
import fr.cyu.schoolmanagementsystem.util.Gmailer;
import fr.cyu.schoolmanagementsystem.util.HashPassword;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        LocalDate currentDate = LocalDate.now();
        // Formater la date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = currentDate.format(formatter);




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

            try {

                AdminDAO adminDAO = new AdminDAO(Admin.class);
                Optional<Admin> admin = adminDAO.findByEmail("schoolmanagementjee@gmail.com");
                Gmailer gmailer = new Gmailer();
                String link = "http://localhost:8080/SchoolManagement"; // Assurez-vous d'utiliser le bon protocole et lien
                gmailer.sendMail(
                        "Nouvelle demande d'inscription d'un étudiant",
                        "Bonjour,<br><br>" +
                                "Une nouvelle demande d'inscription a été soumise par un étudiant sur la plateforme.<br><br>" +
                                "Voici les détails de la demande :<br>" +
                                "- Nom complet : " + student.getFirstname() + " " + student.getLastname() + "<br>" +
                                "- Email : " + student.getEmail() + "<br>" +
                                "- Date de la demande : " + formattedDate + "<br><br>" +
                                "Veuillez examiner cette demande et y répondre dans les plus brefs délais.<br>" +
                                "Connectez-vous ici : <a href='" + link + "'>Accéder à la plateforme</a><br><br>" +
                                "Cordialement,<br>" +
                                "L'équipe de gestion du système.",
                        admin.get().getEmail()
                );
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }


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

            try {

                AdminDAO adminDAO = new AdminDAO(Admin.class);
                Optional<Admin> admin = adminDAO.findByEmail("schoolmanagementjee@gmail.com");
                Gmailer gmailer = new Gmailer();
                String link = "http://localhost:8080/SchoolManagement"; // Assurez-vous d'utiliser le bon protocole et lien
                gmailer.sendMail(
                        "Nouvelle demande d'inscription d'un professeur",
                        "Bonjour,<br><br>" +
                                "Une nouvelle demande d'inscription a été soumise par un enseignant sur la plateforme.<br><br>" +
                                "Voici les détails de la demande :<br>" +
                                "- Nom complet : " + teacher.getFirstname() + " " + teacher.getLastname() + "<br>" +
                                "- Email : " + teacher.getEmail() + "<br>" +
                                "- Date de la demande : " + formattedDate + "<br><br>" +
                                "Veuillez examiner cette demande et y répondre dans les plus brefs délais.<br>" +
                                "Connectez-vous ici : <a href='" + link + "'>Accéder à la plateforme</a><br><br>" +
                                "Cordialement,<br>" +
                                "L'équipe de gestion du système.",
                        admin.get().getEmail()
                );
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

            response.sendRedirect(request.getContextPath() + "/login?flashMessage=teacherRequestSubmitted");


            return;
        }

        // Si le type d'utilisateur est invalide

        response.sendRedirect(request.getContextPath() + "/login?flashMessage=invalidUserType");
    }
}
