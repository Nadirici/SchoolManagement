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

            // Vérifiez si l'utilisateur est authentifié
            if (session == null || session.getAttribute("isAuthenticated") == null || !(Boolean) session.getAttribute("isAuthenticated")) {
                response.sendRedirect(request.getContextPath() + "/auth?flashError=unauthenticated");
                return false;
            }

            // Récupérer le type d'utilisateur de la session
            String userType = (String) session.getAttribute("userType");
            UUID userId = (UUID) session.getAttribute("userId");

            // Vérifiez les droits d'accès en fonction de l'URL
            String requestURI = request.getRequestURI();

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



