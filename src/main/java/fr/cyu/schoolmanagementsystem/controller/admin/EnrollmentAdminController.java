package fr.cyu.schoolmanagementsystem.controller.admin;

import fr.cyu.schoolmanagementsystem.controller.Routes;
import fr.cyu.schoolmanagementsystem.dao.AssignmentDAO;
import fr.cyu.schoolmanagementsystem.dao.CourseDAO;
import fr.cyu.schoolmanagementsystem.dao.EnrollmentDAO;
import fr.cyu.schoolmanagementsystem.dao.StudentDAO;
import fr.cyu.schoolmanagementsystem.entity.*;
import fr.cyu.schoolmanagementsystem.service.AssignmentService;
import fr.cyu.schoolmanagementsystem.service.CourseService;
import fr.cyu.schoolmanagementsystem.service.EnrollmentService;
import fr.cyu.schoolmanagementsystem.service.StudentService;
import fr.cyu.schoolmanagementsystem.service.stats.EnrollmentStatsService;
import fr.cyu.schoolmanagementsystem.util.CompositeStats;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet(Routes.ADMIN_ENROLLMENTS)
public class EnrollmentAdminController extends HttpServlet {

    private EnrollmentService enrollmentService;
    private EnrollmentStatsService enrollmentStatsService;
    private AssignmentService assignmentService;
    private StudentService studentService;
    private CourseService courseService;

    @Override
    public void init() throws ServletException {
        enrollmentService = new EnrollmentService(new EnrollmentDAO(Enrollment.class));
        enrollmentStatsService = new EnrollmentStatsService();
        assignmentService = new AssignmentService(new AssignmentDAO(Assignment.class));
        studentService = new StudentService(new StudentDAO(Student.class));
        courseService = new CourseService(new CourseDAO(Course.class));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam != null) {
            viewEnrollment(request, response);
        } else {
            listEnrollments(request, response);
        }
    }

    private void listEnrollments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Enrollment> enrollments = enrollmentService.getAll();

            request.setAttribute("enrollments", enrollments);

            request.getRequestDispatcher("/WEB-INF/views/admin/enrollments/enrollments.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching enrollments");
        }
    }

    private void viewEnrollment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UUID id = UUID.fromString(request.getParameter("id"));
        try {
            Enrollment enrollment = enrollmentService.getById(id);
            CompositeStats enrollmentStats = enrollmentStatsService.getStatsForEnrollment(id);
            Map<Assignment, Grade> assignmentGrade = assignmentService.getAssignmentsAndGradesForEnrollment(id);

            request.setAttribute("enrollment", enrollment);
            request.setAttribute("enrollmentStats", enrollmentStats);
            request.setAttribute("assignmentGrade", assignmentGrade);

            request.getRequestDispatcher("/WEB-INF/views/admin/enrollments/enrollment-details.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Enrollment not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");

        if ("DELETE".equalsIgnoreCase(method)) {
            String idParam = request.getParameter("id");
            if (idParam != null) {
                UUID id = UUID.fromString(idParam);
                try {
                    enrollmentService.delete(id);
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Enrollment not found");
                }
            }
        } else {
            String studentId = request.getParameter("studentId");
            String courseId = request.getParameter("courseId");

            Enrollment newEnrollment = new Enrollment();
            newEnrollment.setStudent(studentService.getById(UUID.fromString(studentId)));
            newEnrollment.setCourse(courseService.getById(UUID.fromString(courseId)));

            enrollmentService.add(newEnrollment);
        }
        response.sendRedirect(request.getContextPath() + Routes.ADMIN_ENROLLMENTS);
    }

}
