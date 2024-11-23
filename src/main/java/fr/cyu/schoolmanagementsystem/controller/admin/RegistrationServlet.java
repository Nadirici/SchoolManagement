package fr.cyu.schoolmanagementsystem.controller.admin;

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
import java.util.Base64;
import java.util.Optional;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final RegistrationRequestService registrationRequestService;

    // Constructeur pour initialiser les services nécessaires
    public RegistrationServlet() {
        StudentDAO studentDAO = new StudentDAO(Student.class);
        this.studentService = new StudentService(studentDAO, new EnrollmentService(new EnrollmentDAO(Enrollment.class)),new GradeService(new GradeDAO(Grade.class)));

        TeacherDAO teacherDAO = new TeacherDAO(Teacher.class);
        this.teacherService = new TeacherService(teacherDAO);

        RegistrationRequestDAO registrationRequestDAO = new RegistrationRequestDAO(RegistrationRequest.class);
        this.registrationRequestService = new RegistrationRequestService(registrationRequestDAO);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType");
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String dateOfBirth = request.getParameter("dateOfBirth"); // Format: YYYY-MM-DD

        Optional<Student> studentOptional = studentService.getStudentByEmail(email);
        Optional<Teacher> teacherOptional = teacherService.getTeacherByEmail(email);
        if ("student".equalsIgnoreCase(userType)) {

            if (studentOptional.isPresent() || teacherOptional.isPresent()) {
                response.sendRedirect(request.getContextPath() + "/login?flashMessage=emailAlreadyExists");
                return;
            }

            // Enregistrer l'étudiant
            String salt = Base64.getEncoder().encodeToString(HashPassword.generateSalt());
            String hashedPassword = HashPassword.hashPassword(password, Base64.getDecoder().decode(salt));
            Student student = new Student(firstName, lastName, email, hashedPassword, salt);
            studentService.registerStudent(student);

            // Créer et enregistrer la demande d'inscription
            registrationRequestService.saveRequest(student);

            response.sendRedirect(request.getContextPath() + "/login?flashMessage=studentRequestSubmitted");



            return;
        }

        if ("teacher".equalsIgnoreCase(userType)) {

            if (teacherOptional.isPresent() || studentOptional.isPresent()) {
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

            Teacher teacher = new Teacher(firstName, lastName, email, hashedPassword, salt);
            teacherService.registerTeacher(teacher);

            // Créer et enregistrer la demande d'inscription
            registrationRequestService.saveRequest(teacher);

            response.sendRedirect(request.getContextPath() + "/login?flashMessage=teacherRequestSubmitted");


            return;
        }

        // Si le type d'utilisateur est invalide

        response.sendRedirect(request.getContextPath() + "/login?flashMessage=invalidUserType");
    }
}
