package fr.cyu.schoolmanagementsystem.controller.admin;

import fr.cyu.schoolmanagementsystem.dao.TeacherDAO;
import fr.cyu.schoolmanagementsystem.entity.Teacher;
import fr.cyu.schoolmanagementsystem.service.TeacherService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/teacher/*")
public class TeacherDashboardServlet extends HttpServlet {

    private final TeacherService teacherService;

    public TeacherDashboardServlet() {
        TeacherDAO teacherDAO = new TeacherDAO(Teacher.class);
        this.teacherService = new TeacherService(teacherDAO);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo(); // récupère le {id} de l'URL
        if (pathInfo != null && pathInfo.startsWith("/")) {
            pathInfo = pathInfo.substring(1); // Supprimer le premier "/"
        }

        try {
            assert pathInfo != null;
            UUID teacherId = UUID.fromString(pathInfo); // L'id de l'enseignant est supposé être un UUID
            Teacher teacher = teacherService.getById(teacherId);
            if (teacher != null) {
                // Si l'enseignant existe, vous pouvez stocker dans la session ou passer des attributs
                request.setAttribute("teacher", teacher);
                request.getRequestDispatcher("/jsp/teacher/dashboard.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/jsp/auth.jsp?flashMessage=teacherNotFound");
            }
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/jsp/auth.jsp?flashMessage=invalidTeacherId");
        }
    }
}
