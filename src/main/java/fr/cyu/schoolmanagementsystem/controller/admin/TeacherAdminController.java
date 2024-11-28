package fr.cyu.schoolmanagementsystem.controller.admin;

import fr.cyu.schoolmanagementsystem.controller.Routes;
import fr.cyu.schoolmanagementsystem.dao.RegistrationRequestDAO;
import fr.cyu.schoolmanagementsystem.dao.TeacherDAO;
import fr.cyu.schoolmanagementsystem.entity.Admin;
import fr.cyu.schoolmanagementsystem.entity.RegistrationRequest;
import fr.cyu.schoolmanagementsystem.entity.Teacher;
import fr.cyu.schoolmanagementsystem.service.RequestService;
import fr.cyu.schoolmanagementsystem.service.TeacherService;
import fr.cyu.schoolmanagementsystem.util.HashPassword;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@WebServlet(Routes.ADMIN_TEACHERS + "/*")
public class TeacherAdminController extends HttpServlet {

    private TeacherService teacherService;
    private RequestService requestService;

    @Override
    public void init() throws ServletException {
        teacherService = new TeacherService(new TeacherDAO(Teacher.class));
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

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                listTeachers(request, response);
            } else {
                String[] pathParts = pathInfo.split("/");

                if (pathParts.length == 2) {
                    String idSegment = pathParts[1];
                    viewTeacher(request, response, idSegment);
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL format");
                }
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }

    private void listTeachers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Teacher> teachers = teacherService.getAllVerified();

            request.setAttribute("teachers", teachers);

            request.getRequestDispatcher("/WEB-INF/views/admin/teachers/teachers.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching teachers");
        }
    }

    private void viewTeacher(HttpServletRequest request, HttpServletResponse response, String idSegment) throws ServletException, IOException {
        UUID id = UUID.fromString(idSegment);
        try {
            Teacher teacher = teacherService.getById(id);

            request.setAttribute("teacher", teacher);

            request.getRequestDispatcher("/WEB-INF/views/admin/teachers/teacher-details.jsp").forward(request, response);
        } catch (EntityNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Teacher not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");

        if ("PUT".equalsIgnoreCase(method)) {
            handleUpdateTeacher(request, response);
        } else if ("DELETE".equalsIgnoreCase(method)) {
            handleDeleteTeacher(request, response);
            response.sendRedirect(request.getContextPath() + Routes.ADMIN_TEACHERS);
        } else {
            handleAddTeacher(request, response);
            response.sendRedirect(request.getContextPath() + Routes.ADMIN_TEACHERS);
        }
    }

    private void handleAddTeacher(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String department = request.getParameter("department");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String salt = Base64.getEncoder().encodeToString(HashPassword.generateSalt());
        String hashedPassword = HashPassword.hashPassword(password, Base64.getDecoder().decode(salt));

        Teacher teacher = new Teacher(firstname, lastname, email, hashedPassword, department, salt, true);
        UUID id = teacherService.add(teacher);

        RegistrationRequest registrationRequest = new RegistrationRequest(teacher);
        requestService.add(registrationRequest);
    }

    private void handleUpdateTeacher(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UUID id = UUID.fromString(request.getParameter("id"));
        try {
            Teacher teacherToUpdate = teacherService.getById(id);
            teacherToUpdate.setFirstname(request.getParameter("firstname"));
            teacherToUpdate.setLastname(request.getParameter("lastname"));
            teacherToUpdate.setEmail(request.getParameter("email"));
            teacherToUpdate.setDepartment(request.getParameter("department"));

            teacherService.update(teacherToUpdate);
            response.sendRedirect(request.getContextPath() + Routes.ADMIN_TEACHERS + "/" + teacherToUpdate.getId());
        } catch (EntityNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Teacher not found");
        }
    }

    private void handleDeleteTeacher(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UUID id = UUID.fromString(request.getParameter("id"));

        try {
            teacherService.delete(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (EntityNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Teacher not found");
        }
    }
}
