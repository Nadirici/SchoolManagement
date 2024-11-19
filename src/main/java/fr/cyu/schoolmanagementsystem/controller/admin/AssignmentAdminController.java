package fr.cyu.schoolmanagementsystem.controller.admin;

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
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet("/assignments")
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
        String idParam = request.getParameter("id");

        if (idParam != null) {
            UUID id = UUID.fromString(idParam);
            try {
                Assignment assignment = assignmentService.getById(id);
                CompositeStats assignmentStats = assignmentStatsService.getStatsForAssignment(id);
                Map<Enrollment, Grade> enrollmentGradeMap = enrollmentService.getEnrollmentsAndGradesForAssignment(id);
                request.setAttribute("assignment", assignment);
                request.setAttribute("assignmentStats", assignmentStats);
                request.setAttribute("enrollmentGrade", enrollmentGradeMap);
                request.getRequestDispatcher("/WEB-INF/views/admin/assignments/assignment-details.jsp").forward(request, response);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Assignment not found");
            }
        } else {
            List<Assignment> assignments = assignmentService.getAll();
            List<Course> availableCourses = courseService.getAll();
            request.setAttribute("assignments", assignments);
            request.setAttribute("availableCourses", availableCourses);
            request.getRequestDispatcher("/WEB-INF/views/admin/assignments/assignments.jsp").forward(request, response);
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
        response.sendRedirect(request.getContextPath() + "/assignments");
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
        String[] gradeIds = request.getParameterValues("gradeId");
        String[] gradeScores = request.getParameterValues("gradeScore");

        for (int i = 0; i < gradeIds.length; i++) {
            UUID gradeUUID = UUID.fromString(gradeIds[i]);
            Grade grade = gradeService.getById(gradeUUID);
            grade.setScore(Double.parseDouble(gradeScores[i]));
            gradeService.update(grade);
        }
    }

}
