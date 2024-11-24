package fr.cyu.schoolmanagementsystem.controller.admin;

import fr.cyu.schoolmanagementsystem.controller.Routes;
import fr.cyu.schoolmanagementsystem.dao.*;
import fr.cyu.schoolmanagementsystem.entity.*;
import fr.cyu.schoolmanagementsystem.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(Routes.ADMIN_REQUESTS)
public class RequestAdminController extends HttpServlet {

    private RequestService requestService;

    @Override
    public void init() throws ServletException {
        requestService = new RequestService(new RegistrationRequestDAO(RegistrationRequest.class));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Admin admin = AdminServlet.checkAdminSession(request, response);

        if (admin != null) {
            // Ajouter l'admin en tant qu'attribut de la requête
            request.setAttribute("admin", admin);
        } else {
            response.sendRedirect(request.getContextPath() + "/login?flashMessage=notAuthorized");
        }

        List<RegistrationRequest> pendingTeacherRequests = requestService.getPendingTeacherRequests();
        List<RegistrationRequest> pendingStudentRequests = requestService.getPendingStudentRequests();

        request.setAttribute("pendingTeacherRequests", pendingTeacherRequests);
        request.setAttribute("pendingStudentRequests", pendingStudentRequests);

        request.getRequestDispatcher("/WEB-INF/views/admin/requests/requests.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        if (idParam != null) {
            UUID id = UUID.fromString(idParam);

            try {
                if ("approve".equalsIgnoreCase(action)) {
                    requestService.approveRequest(id);
                    response.sendRedirect(request.getContextPath() + Routes.ADMIN_REQUESTS + "?status=approved");
                } else if ("reject".equalsIgnoreCase(action)) {
                    requestService.rejectRequest(id);
                    response.sendRedirect(request.getContextPath() + Routes.ADMIN_REQUESTS + "?status=rejected");
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Request not found");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request ID is required");
        }
    }
}
