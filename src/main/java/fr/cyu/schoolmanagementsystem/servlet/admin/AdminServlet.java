package fr.cyu.schoolmanagementsystem.servlet.admin;

import fr.cyu.schoolmanagementsystem.dao.AdminDAO;
import fr.cyu.schoolmanagementsystem.dao.StudentDAO;
import fr.cyu.schoolmanagementsystem.dao.TeacherDAO;
import fr.cyu.schoolmanagementsystem.entity.Admin;
import fr.cyu.schoolmanagementsystem.entity.Student;
import fr.cyu.schoolmanagementsystem.entity.Teacher;
import fr.cyu.schoolmanagementsystem.service.AdminService;
import fr.cyu.schoolmanagementsystem.service.StudentService;
import fr.cyu.schoolmanagementsystem.service.TeacherService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {

    private static AdminService adminService;
    private StudentService studentService;
    private TeacherService teacherService;

    @Override
    public void init() throws ServletException {
        adminService = new AdminService(new AdminDAO(Admin.class));
        studentService = new StudentService(new StudentDAO(Student.class));
        teacherService = new TeacherService(new TeacherDAO(Teacher.class));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Admin admin = checkAdminSession(request, response);

        if (admin != null) {
            // Ajouter l'admin en tant qu'attribut de la requête
            int verifiedStudentsCounter = studentService.getAllVerified().size();
            int verifiedTeachersCounter = teacherService.getAllVerified().size();
            double percentageStudentsGreaterThanTen = studentService.getPercentageOfStudentsWithGlobalAverageGreaterThanTen();

            request.setAttribute("verifiedStudentsCounter", verifiedStudentsCounter);
            request.setAttribute("verifiedTeachersCounter", verifiedTeachersCounter);
            request.setAttribute("percentageStudentsGreaterThanTen", percentageStudentsGreaterThanTen);
            request.setAttribute("admin", admin);

            request.getRequestDispatcher("/WEB-INF/views/admin/index.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login?flashMessage=notAuthorized");
        }

    }

    // Méthode statique pour vérifier la session et récupérer l'admin
    public static Admin checkAdminSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Vérifier si l'utilisateur est authentifié
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("isAuthenticated") == null || !(boolean) session.getAttribute("isAuthenticated")) {
            return null;
        }

        // Vérifier si l'utilisateur est un administrateur
        String userType = (String) session.getAttribute("userType");
        if (!"admin".equals(userType)) {

            return null;
        }

        // Si l'ID dans la session est un UUID, le convertir en UUID
        UUID sessionId = (UUID) session.getAttribute("userId");

        // Charger l'admin depuis la base de données
        Admin admin = adminService.getById(sessionId);
        if (admin == null) {
            // Si l'admin n'existe pas, renvoyer une erreur
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Admin not found");
            return null;
        }

        return admin;  // Retourner l'admin si la session est valide
    }
}
