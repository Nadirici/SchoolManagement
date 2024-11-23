package fr.cyu.schoolmanagementsystem.controller.admin;

import fr.cyu.schoolmanagementsystem.dao.*;
import fr.cyu.schoolmanagementsystem.entity.*;
import fr.cyu.schoolmanagementsystem.service.*;
import fr.cyu.schoolmanagementsystem.util.HashPassword;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;


@WebServlet(value = "/login" ,name = "AuthJsp")
public class LoginServlet extends HttpServlet {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final AdminService adminService;

    // Constructeur pour l'initialisation des services
    public LoginServlet() {
        AdminDAO adminDAO = new AdminDAO(Admin.class);
        this.adminService = AdminService.createInstance(adminDAO);
        TeacherDAO teacherDAO = new TeacherDAO(Teacher.class);
        this.teacherService = new TeacherService(teacherDAO);
        StudentDAO studentDAO = new StudentDAO(Student.class);
        this.studentService = new StudentService(studentDAO, new EnrollmentService(new EnrollmentDAO(Enrollment.class)), new GradeService(new GradeDAO(Grade.class)));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response); // Afficher le formulaire de connexion
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");


        try
        {
        // Vérification de l'étudiant
        Optional<Student> studentOptional = studentService.getStudentByEmail(email);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            if (HashPassword.verifyPassword(password, student.getPassword(), student.getSalt())) {
                if (student.isVerified()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("userId", student.getId());
                    session.setAttribute("userType", "student");
                    session.setAttribute("isAuthenticated", true);
                    response.sendRedirect(request.getContextPath() +"/student/" + student.getId());
                    return;
                } else {

                    response.sendRedirect(request.getContextPath() + "/login?flashMessage=unVerifiedStudent");
                    return;
                }
            }
        }

        // Vérification de l'enseignant
        Optional<Teacher> teacherOptional = teacherService.getTeacherByEmail(email);
        if (teacherOptional.isPresent()) {
            Teacher teacher = teacherOptional.get();
            if (HashPassword.verifyPassword(password, teacher.getPassword(), teacher.getSalt())) {
                if (teacher.isVerified()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("userId", teacher.getId());
                    session.setAttribute("userType", "teacher");
                    session.setAttribute("isAuthenticated", true);
                    session.setAttribute("teacher", teacher);
                    response.sendRedirect(request.getContextPath() +"/teachers/" + teacher.getId());
                    return;
                } else {

                    response.sendRedirect(request.getContextPath() + "/login?flashMessage=unVerifiedTeacher");
                    return;
                }
            }
        }

        // Vérification de l'administrateur
        Admin admin = adminService.getAdminByEmail(email);
        if (admin != null && HashPassword.verifyPassword(password, admin.getPassword(), admin.getSalt())) {
            HttpSession session = request.getSession();
            session.setAttribute("userId", admin.getId());
            session.setAttribute("userType", "admin");
            session.setAttribute("isAuthenticated", true);
            session.setAttribute("admin", admin);
            // Redirection vers l'URL dynamique de l'administrateur
            response.sendRedirect(request.getContextPath() + "/admin/" + admin.getId()); // Redirection dynamique
            return;
        }

        // Si aucune vérification n'a abouti, rediriger avec un message d'erreur
        response.sendRedirect(request.getContextPath() + "/login?flashMessage=incorrectEmailOrPassword");
        }catch (EntityNotFoundException e){
            response.sendRedirect(request.getContextPath() + "/login?flashMessage=incorrectEmailOrPassword");
        }


    }
}
