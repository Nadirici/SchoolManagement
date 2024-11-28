package fr.cyu.schoolmanagementsystem.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Invalider la session actuelle pour supprimer toutes les informations
        request.getSession().invalidate();

        // Rediriger l'utilisateur vers la page de connexion
        response.sendRedirect(request.getContextPath() + "/login"); // Vous pouvez ajuster l'URL selon votre page de connexion
    }
}
