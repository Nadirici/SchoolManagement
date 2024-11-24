package fr.cyu.schoolmanagementsystem.controller.admin;

import fr.cyu.schoolmanagementsystem.controller.Routes;
import fr.cyu.schoolmanagementsystem.dao.AssignmentDAO;
import fr.cyu.schoolmanagementsystem.dao.CourseDAO;
import fr.cyu.schoolmanagementsystem.dao.EnrollmentDAO;
import fr.cyu.schoolmanagementsystem.dao.GradeDAO;
import fr.cyu.schoolmanagementsystem.entity.*;
import fr.cyu.schoolmanagementsystem.service.AssignmentService;
import fr.cyu.schoolmanagementsystem.service.CourseService;
import fr.cyu.schoolmanagementsystem.service.EnrollmentService;
import fr.cyu.schoolmanagementsystem.service.GradeService;
import fr.cyu.schoolmanagementsystem.service.stats.AssignmentStatsService;
import fr.cyu.schoolmanagementsystem.util.CompositeStats;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet(Routes.ADMIN_ASSIGNMENTS + "/*")
public class AssignmentAdminController extends HttpServlet {

    private AssignmentService assignmentService;
    private AssignmentStatsService assignmentStatsService;
    private EnrollmentService enrollmentService;
    private CourseService courseService;
    private GradeService gradeService;

    @Override
    public void init() throws ServletException {
        assignmentService = new AssignmentService(new AssignmentDAO(Assignment.class));
        assignmentStatsService = new AssignmentStatsService();
        enrollmentService = new EnrollmentService(new EnrollmentDAO(Enrollment.class));
        courseService = new CourseService(new CourseDAO(Course.class));
        gradeService = new GradeService(new GradeDAO(Grade.class));
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
                listAssignments(request, response);
            } else {
                String[] pathParts = pathInfo.split("/");

                if (pathParts.length == 2) {
                    String idSegment = pathParts[1];
                    viewAssignment(request, response, idSegment);
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL format");
                }
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }

    private void listAssignments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Assignment> assignments = assignmentService.getAll();
            List<Course> availableCourses = courseService.getAll();

            request.setAttribute("assignments", assignments);
            request.setAttribute("availableCourses", availableCourses);

            request.getRequestDispatcher("/WEB-INF/views/admin/assignments/assignments.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching assignments");
        }
    }

    private void viewAssignment(HttpServletRequest request, HttpServletResponse response, String idSegment) throws ServletException, IOException {
        try {
            UUID id = UUID.fromString(idSegment);
            Assignment assignment = assignmentService.getById(id);
            CompositeStats assignmentStats = assignmentStatsService.getStatsForAssignment(id);
            Map<Enrollment, Grade> enrollmentGrade = enrollmentService.getEnrollmentsAndGradesForAssignment(id);
            request.setAttribute("assignment", assignment);
            request.setAttribute("assignmentStats", assignmentStats);
            request.setAttribute("enrollmentGrade", enrollmentGrade);
            request.getRequestDispatcher("/WEB-INF/views/admin/assignments/assignment-details.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Assignment not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");
        String action = request.getParameter("action");

        if ("DELETE".equalsIgnoreCase(method)) {
            deleteAssignment(request, response);
        } else if ("PUT".equalsIgnoreCase(method)) {
            if ("saveGrades".equalsIgnoreCase(action)) {
                saveGrades(request, response);
            }
        } else {
            createAssignment(request, response);
        }
        response.sendRedirect(request.getContextPath() + Routes.ADMIN_ASSIGNMENTS);
    }

    private void deleteAssignment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam != null) {
            UUID id = UUID.fromString(idParam);
            try {
                assignmentService.delete(id);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Assignment not found");
            }
        }
    }

    private void createAssignment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String assignmentTitle = request.getParameter("title");
        String assignmentDescription = request.getParameter("description");
        String assignmentCoefficient = request.getParameter("coefficient");
        String courseId = request.getParameter("courseId");

        if (assignmentTitle != null && !assignmentTitle.isEmpty() && assignmentDescription != null && !assignmentDescription.isEmpty() && courseId != null) {
            Assignment assignment = new Assignment();
            assignment.setTitle(assignmentTitle);
            assignment.setDescription(assignmentDescription);
            assignment.setCoefficient(Double.parseDouble(assignmentCoefficient));
            Course course = courseService.getById(UUID.fromString(courseId));
            assignment.setCourse(course);

            assignmentService.add(assignment);

            List<Enrollment> enrollments = enrollmentService.getEnrollmentsForCourse(course.getId());

            for (Enrollment enrollment : enrollments) {
                Grade grade = new Grade();
                grade.setAssignment(assignment);
                grade.setEnrollment(enrollment);
                grade.setScore(0.0);
                gradeService.add(grade);
            }

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields for assignment creation");
        }
    }

    private void saveGrades(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, String[]> parameterMap = request.getParameterMap();

        Map<UUID, Double> gradesMap = new HashMap<>();

        parameterMap.forEach((key, value) -> {
            if (key.startsWith("grades[")) {
                String id = key.substring(7, key.length() - 1);
                UUID gradeId = UUID.fromString(id);
                Double score = Double.parseDouble(value[0]);
                gradesMap.put(gradeId, score);
            }
        });

        gradesMap.forEach((gradeId, score) -> {
            Grade grade = gradeService.getById(gradeId);
            grade.setScore(score);
            gradeService.update(grade);
        });
    }

}
