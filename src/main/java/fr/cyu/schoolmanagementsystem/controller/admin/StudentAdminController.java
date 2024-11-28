package fr.cyu.schoolmanagementsystem.controller.admin;

import fr.cyu.schoolmanagementsystem.controller.Routes;
import fr.cyu.schoolmanagementsystem.dao.AdminDAO;
import fr.cyu.schoolmanagementsystem.dao.CourseDAO;
import fr.cyu.schoolmanagementsystem.dao.RegistrationRequestDAO;
import fr.cyu.schoolmanagementsystem.dao.StudentDAO;
import fr.cyu.schoolmanagementsystem.entity.*;
import fr.cyu.schoolmanagementsystem.service.AdminService;
import fr.cyu.schoolmanagementsystem.service.CourseService;
import fr.cyu.schoolmanagementsystem.service.RequestService;
import fr.cyu.schoolmanagementsystem.service.StudentService;
import fr.cyu.schoolmanagementsystem.service.stats.EnrollmentStatsService;
import fr.cyu.schoolmanagementsystem.service.stats.StudentStatsService;
import fr.cyu.schoolmanagementsystem.util.CompositeStats;
import fr.cyu.schoolmanagementsystem.util.HashPassword;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet(Routes.ADMIN_STUDENTS + "/*")
public class StudentAdminController extends HttpServlet {

    private StudentService studentService;
    private RequestService requestService;
    private CourseService courseService;
    private EnrollmentStatsService enrollmentStatsService;
    private StudentStatsService studentStatsService;
    private AdminService adminService;

    @Override
    public void init() throws ServletException {
        studentService = new StudentService(new StudentDAO(Student.class));
        requestService = new RequestService(new RegistrationRequestDAO(RegistrationRequest.class));
        courseService = new CourseService(new CourseDAO(Course.class));
        enrollmentStatsService =  new EnrollmentStatsService();
        studentStatsService =  new StudentStatsService();
        adminService = new AdminService(new AdminDAO(Admin.class));
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
                listStudents(request, response);
            } else {
                String[] pathParts = pathInfo.split("/");

                if (pathParts.length == 2) {
                    String idSegment = pathParts[1];
                    viewStudent(request, response, idSegment);
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL format");
                }
            }
        } catch (Exception e) {
            // En cas d'erreur interne
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }

    private void listStudents(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Student> students = studentService.getAllVerified();

            request.setAttribute("students", students);

            request.getRequestDispatcher("/WEB-INF/views/admin/students/students.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching students");
        }
    }

    private void viewStudent(HttpServletRequest request, HttpServletResponse response, String idSegment) throws ServletException, IOException {
        try {
            UUID id = UUID.fromString(idSegment);
            Student student = studentService.getById(id);
            List<Course> availableCourses = courseService.getAllNotEnroll(student.getId());
            CompositeStats studentStats = studentStatsService.getStatsForStudent(student.getId());
            Map<Enrollment, CompositeStats> enrollmentStats = enrollmentStatsService.getEnrollmentsAndStatsForStudent(student.getId());

            request.setAttribute("student", student);
            request.setAttribute("availableCourses", availableCourses);
            request.setAttribute("studentStats", studentStats);
            request.setAttribute("enrollmentStats", enrollmentStats);

            request.getRequestDispatcher("/WEB-INF/views/admin/students/student-details.jsp").forward(request, response);
        } catch (EntityNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Student not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");

        if ("PUT".equalsIgnoreCase(method)) {
            handleUpdateStudent(request, response);
        } else if ("DELETE".equalsIgnoreCase(method)) {
            handleDeleteStudent(request, response);
            response.sendRedirect(request.getContextPath() + Routes.ADMIN_STUDENTS);
        } else {
            handleAddStudent(request, response);
            response.sendRedirect(request.getContextPath() + Routes.ADMIN_STUDENTS);
        }
    }

    private void handleAddStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String dateOfBirthStr = request.getParameter("dateOfBirth");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStr);

        String salt = Base64.getEncoder().encodeToString(HashPassword.generateSalt());
        String hashedPassword = HashPassword.hashPassword(password, Base64.getDecoder().decode(salt));

        Student student = new Student(firstname, lastname, dateOfBirth, email, hashedPassword, salt, true);
        UUID id = studentService.add(student);
    }

    private void handleUpdateStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UUID id = UUID.fromString(request.getParameter("id"));
        try {
            Student studentToUpdate = studentService.getById(id);
            studentToUpdate.setFirstname(request.getParameter("firstname"));
            studentToUpdate.setLastname(request.getParameter("lastname"));
            studentToUpdate.setEmail(request.getParameter("email"));
            studentToUpdate.setDateOfBirth(LocalDate.parse(request.getParameter("dateOfBirth")));

            studentService.update(studentToUpdate);
            response.sendRedirect(request.getContextPath() + Routes.ADMIN_STUDENTS + "/" + studentToUpdate.getId());
        } catch (EntityNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Student not found");
        }
    }

    private void handleDeleteStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UUID id = UUID.fromString(request.getParameter("id"));

        try {
            studentService.delete(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (EntityNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Student not found");
        }
    }
}
