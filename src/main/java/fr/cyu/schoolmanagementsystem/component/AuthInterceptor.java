package fr.cyu.schoolmanagementsystem.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;


@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();

        // Vérifiez si l'utilisateur est authentifié
        if (session == null || session.getAttribute("isAuthenticated") == null || !(Boolean) session.getAttribute("isAuthenticated")) {
            // Utilisateur non authentifié, redirige vers /auth
            if (requestURI.startsWith("/auth")) {
                return true; // Permet d'accéder à la page /auth
            }
            response.sendRedirect(request.getContextPath() + "/auth?flashMessage=unauthenticated");
            return false;
        }

        // Si l'utilisateur est authentifié et tente d'accéder à /auth, redirection vers le tableau de bord
        if (Boolean.TRUE.equals(session.getAttribute("isAuthenticated")) && requestURI.startsWith("/auth")) {
            String userType = (String) session.getAttribute("userType");
            UUID userId = (UUID) session.getAttribute("userId");

            switch (userType) {
                case "teacher":
                    response.sendRedirect("/teachers/" + userId);
                    break;
                case "student":
                    response.sendRedirect("/students/" + userId);
                    break;
                case "admin":
                    response.sendRedirect("/admin/" + userId);
                    break;
            }
            return false; // Empêche l'accès à /auth si déjà connecté
        }

        // Récupérer le type d'utilisateur de la session
        String userType = (String) session.getAttribute("userType");
        UUID userId = (UUID) session.getAttribute("userId");

        // Accès des administrateurs uniquement
        if (requestURI.startsWith("/admin") && !"admin".equals(userType)) {
            response.sendRedirect(request.getContextPath() + "/noaccess");
            return false;
        }

        // Accès des enseignants uniquement
        if (requestURI.startsWith("/teachers") && !"teacher".equals(userType)) {
            response.sendRedirect(request.getContextPath() + "/noaccess");
            return false;
        }

        // Accès des étudiants uniquement
        if (requestURI.startsWith("/students") && !"student".equals(userType)) {
            response.sendRedirect(request.getContextPath() + "/noaccess");
            return false;
        }

        // Vérification de l'ID de l'utilisateur dans l'URL pour les étudiants et enseignants
        if (requestURI.startsWith("/students/")) {
            String studentIdInUrl = requestURI.split("/")[2]; // Extraire l'ID de l'URL
            if (!userId.toString().equals(studentIdInUrl)) {
                response.sendRedirect(request.getContextPath() + "/noaccess");
                return false;
            }
        }

        if (requestURI.startsWith("/teachers/")) {
            String teacherIdInUrl = requestURI.split("/")[2]; // Extraire l'ID de l'URL
            if (!userId.toString().equals(teacherIdInUrl)) {
                response.sendRedirect(request.getContextPath() + "/noaccess");
                return false;
            }
        }

        if (requestURI.startsWith("/admin/")) {
            String adminIdInUrl = requestURI.split("/")[2]; // Extraire l'ID de l'URL
            if (!userId.toString().equals(adminIdInUrl)) {
                response.sendRedirect(request.getContextPath() + "/noaccess");
                return false;
            }
        }

        return true;
    }
}



