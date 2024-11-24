package fr.cyu.schoolmanagementsystem.controller;

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
        adminService = new AdminService(new AdminDAO(Admin.class));
        studentService = new StudentService(new StudentDAO(Student.class));
        teacherService = new TeacherService(new TeacherDAO(Teacher.class));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/auth.jsp").forward(request, response); // Afficher le formulaire de connexion
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.println(email);
        System.out.println(password);


        try {
            // Vérification de l'étudiant
            Student student = studentService.getByEmail(email);
            System.out.println(student.getEmail());
            if (HashPassword.verifyPassword(password, student.getPassword(), student.getSalt())) {
                System.out.println(student.getPassword());
                if (student.isVerified()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("userId", student.getId());
                    session.setAttribute("userType", "student");
                    session.setAttribute("isAuthenticated", true);
                    System.out.println(session.getAttribute("userId"));
                    response.sendRedirect(request.getContextPath() + "/students");
                    return;
                } else {

                    response.sendRedirect(request.getContextPath() + "/login?flashMessage=unVerifiedStudent");
                    return;
                }
            }
        } catch (EntityNotFoundException _) {
            // On ignore l'erreur
        }

        try {
            // Vérification de l'enseignant
            Teacher teacher = teacherService.getByEmail(email);
            if (HashPassword.verifyPassword(password, teacher.getPassword(), teacher.getSalt())) {
                if (teacher.isVerified()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("userId", teacher.getId());
                    session.setAttribute("userType", "teacher");
                    session.setAttribute("isAuthenticated", true);
                    session.setAttribute("teacher", teacher);
                    response.sendRedirect(request.getContextPath() + "/teachers");
                    return;
                } else {
                    response.sendRedirect(request.getContextPath() + "/login?flashMessage=unVerifiedTeacher");
                    return;
                }
            }
        } catch (EntityNotFoundException _) {
            // On ignore l'erreur
        }

        try {
            // Vérification de l'administrateur
            Admin admin = adminService.getByEmail(email);
            if (admin != null && HashPassword.verifyPassword(password, admin.getPassword(), admin.getSalt())) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", admin.getId());
                session.setAttribute("userType", "admin");
                session.setAttribute("isAuthenticated", true);
                session.setAttribute("admin", admin);
                // Redirection vers l'URL dynamique de l'administrateur
                response.sendRedirect(request.getContextPath() + "/admin"); // Redirection dynamique
                return;
            }
        } catch (EntityNotFoundException _) {
            // On ignore l'erreur
        }

        // Si aucune vérification n'a abouti, rediriger avec un message d'erreur
        response.sendRedirect(request.getContextPath() + "/login?flashMessage=incorrectEmailOrPassword");
    }
}