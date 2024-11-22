package fr.cyu.schoolmanagementsystem.controller.admin;

import fr.cyu.schoolmanagementsystem.dao.AdminDAO;
import fr.cyu.schoolmanagementsystem.entity.Admin;
import fr.cyu.schoolmanagementsystem.service.AdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private static AdminService adminService;

    @Override
    public void init() throws ServletException {
        adminService = new AdminService(new AdminDAO(Admin.class));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Admin admin = checkAdminSession(request, response);

        // Ajouter l'admin en tant qu'attribut de la requête
        request.setAttribute("admin", admin);

        request.getRequestDispatcher("/WEB-INF/views/admin/index.jsp").forward(request, response);
    }

    // Méthode statique pour vérifier la session et récupérer l'admin
    public static Admin checkAdminSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Simulation d'un ID fixe temporaire
        String adminId = "e2437152-2648-41ac-bdd6-5f6f273839b0"; // Exemple d'ID fixe

/*        // Vérifier la session pour l'adminId  (à activer une fois implémentée)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminId") == null) {
            // Rediriger si la session est invalide ou si l'adminId est manquant
            response.sendRedirect(request.getContextPath() + "/login");
            return null;  // Retourner null car la session n'est pas valide
        }

        // Récupérer l'adminId depuis la session
        String adminId = (String) session.getAttribute("adminId");*/

        // Charger l'admin depuis la base de données
        Admin admin = adminService.getById(UUID.fromString(adminId));
        if (admin == null) {
            // Si l'admin n'existe pas, renvoyer une erreur
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Admin not found");
            return null;
        }

        return admin;  // Retourner l'admin si la session est valide
    }
}
