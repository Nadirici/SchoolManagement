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

        // Vérifie si le chemin correspond à "/admin/{adminId}/requests"
        if (pathInfo != null) {
            String[] pathParts = pathInfo.split("/");

            // Si nous avons au moins un segment, vérifier si c'est un ID d'administrateur
            if (pathParts.length > 0) {
                try {
                    UUID adminId = UUID.fromString(pathParts[0]); // L'id de l'admin
                    Admin admin = adminService.getById(adminId);

                    if (admin != null) {
                        // Si le chemin contient "requests", charger les demandes d'inscription
                        //TODO Régler le problème, le servlet ne reconnait pas le path
                        if (pathParts.length > 1 && "requests".equals(pathParts[1])) {

                            List<RegistrationRequest> pendingTeacherRequests = requestService.getPendingTeacherRequests();
                            List<RegistrationRequest> pendingStudentRequests = requestService.getPendingStudentRequests();

                            // Passer les informations à la JSP
                            request.setAttribute("admin", admin);
                            request.setAttribute("pendingTeacherRequests", pendingTeacherRequests);
                            request.setAttribute("pendingStudentRequests", pendingStudentRequests);

                            for (RegistrationRequest registrationRequest : pendingTeacherRequests) {
                                System.out.println(registrationRequest.getTeacher());
                            }



                            request.getRequestDispatcher("/WEB-INF/views/admin/requests/requests.jsp").forward(request, response);
                            return;
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
            }
        }
    }
}



