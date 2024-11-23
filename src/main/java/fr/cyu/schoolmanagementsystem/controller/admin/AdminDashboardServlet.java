package fr.cyu.schoolmanagementsystem.controller.admin;

import fr.cyu.schoolmanagementsystem.dao.AdminDAO;
import fr.cyu.schoolmanagementsystem.dao.EnrollmentDAO;
import fr.cyu.schoolmanagementsystem.dao.GradeDAO;
import fr.cyu.schoolmanagementsystem.dao.RegistrationRequestDAO;
import fr.cyu.schoolmanagementsystem.entity.Admin;
import fr.cyu.schoolmanagementsystem.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.entity.Grade;
import fr.cyu.schoolmanagementsystem.entity.RegistrationRequest;
import fr.cyu.schoolmanagementsystem.service.AdminService;
import fr.cyu.schoolmanagementsystem.service.EnrollmentService;
import fr.cyu.schoolmanagementsystem.service.GradeService;
import fr.cyu.schoolmanagementsystem.service.RequestService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
@WebServlet("/admin/*")
public class AdminDashboardServlet extends HttpServlet {

    private final AdminService adminService;
    private final RequestService requestService;

    public AdminDashboardServlet() {
        AdminDAO adminDAO = new AdminDAO(Admin.class);
        this.adminService = AdminService.createInstance(adminDAO);
        this.requestService = new RequestService(new RegistrationRequestDAO(RegistrationRequest.class), new EnrollmentService(new EnrollmentDAO(Enrollment.class)),new GradeService(new GradeDAO(Grade.class)));
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Vérifier si l'utilisateur est authentifié
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("isAuthenticated") == null || !(boolean) session.getAttribute("isAuthenticated")) {
            response.sendRedirect(request.getContextPath() + "/login?flashMessage=notAuthenticated");
            return;
        }

        // Vérifier si l'utilisateur est un administrateur
        String userType = (String) session.getAttribute("userType");
        if (!"admin".equals(userType)) {
            response.sendRedirect(request.getContextPath() + "/login?flashMessage=notAuthorized");
            return;
        }

        // Vérifier si l'ID dans l'URL correspond à l'ID dans la session
        String urlId = request.getPathInfo().substring(1); // Extrait l'ID de l'URL

        if (urlId == null) {
            response.sendRedirect(request.getContextPath() + "/login?flashMessage=missingId");
            return;
        }

        // Si l'ID dans la session est un UUID, le convertir en UUID
        UUID sessionId = (UUID) session.getAttribute("userId");

        System.out.println("Session ID: " + sessionId);
        System.out.println("URL ID: " + urlId);
        try {
            // Convertir l'ID de l'URL en UUID
            UUID urlUUID = UUID.fromString(urlId);

            // Comparer les UUID
            if (!urlUUID.equals(sessionId)) {
                response.sendRedirect(request.getContextPath() + "/login?flashMessage=incorrectAdminId");
                return;
            }
        } catch (IllegalArgumentException e) {
            // Si l'ID dans l'URL n'est pas un UUID valide, redirection vers une page d'erreur
            response.sendRedirect(request.getContextPath() + "/login?flashMessage=invalidIdFormat");
            return;
        }

        String pathInfo = request.getPathInfo(); // récupère le {id} de l'URL
        if (pathInfo != null && pathInfo.startsWith("/")) {
            pathInfo = pathInfo.substring(1); // Supprimer le premier "/"
        }

        // Affichage des segments du chemin avec leur index
        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");

            // Affiche chaque partie du chemin avec son index
            for (int i = 0; i < pathParts.length; i++) {
                System.out.println("Path Part " + i + ": " + pathParts[i]);
            }

            // Si nous avons au moins un segment, vérifier si c'est un ID d'administrateur
            if (pathParts.length > 1) {
                try {
                    // Accédez à l'ID de l'administrateur
                    UUID adminId = UUID.fromString(pathParts[0]); // L'ID de l'administrateur
                    Admin admin = adminService.getById(adminId);

                    if (admin != null) {




                        if (pathParts.length >= 2) {
                            String userId = pathParts[0];  // {userId}
                            String action = pathParts[1];  // 'requests'
                            if ("requests".equals(pathParts[1])) {
                                List<RegistrationRequest> pendingTeacherRequests = requestService.getPendingTeacherRequests();
                                List<RegistrationRequest> pendingStudentRequests = requestService.getPendingStudentRequests();

                                // Passer les informations à la JSP
                                request.setAttribute("admin", admin);
                                request.setAttribute("pendingTeacherRequests", pendingTeacherRequests);
                                request.setAttribute("pendingStudentRequests", pendingStudentRequests);

                                request.getRequestDispatcher("/WEB-INF/views/admin/requests/requests.jsp").forward(request, response);
                                return;
                            }

                        // Vérifier si le chemin contient "requests"

                        } else {
                            request.setAttribute("admin", admin);
                            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
                        }
                    } else {
                        response.sendRedirect(request.getContextPath() + "/jsp/login?flashMessage=adminNotFound");
                    }
                } catch (IllegalArgumentException e) {
                    response.sendRedirect(request.getContextPath() + "/jsp/login?flashMessage=invalidAdminId");
                }
            } else {
                // Traitez le cas où il n'y a pas de "requests" dans le chemin
                response.sendRedirect(request.getContextPath() + "/login?flashMessage=invalidPath");
            }

        }
    }

}



