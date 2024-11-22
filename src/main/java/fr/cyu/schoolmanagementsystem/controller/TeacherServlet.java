package fr.cyu.schoolmanagementsystem.controller;

import fr.cyu.schoolmanagementsystem.dao.*;
import fr.cyu.schoolmanagementsystem.entity.*;
import fr.cyu.schoolmanagementsystem.service.*;
import fr.cyu.schoolmanagementsystem.service.stats.AssignmentStatsService;
import fr.cyu.schoolmanagementsystem.service.stats.CourseStatsService;
import fr.cyu.schoolmanagementsystem.service.stats.EnrollmentStatsService;
import fr.cyu.schoolmanagementsystem.util.CompositeStats;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

@WebServlet(Routes.TEACHER + "/*")
public class TeacherServlet extends HttpServlet {

    private TeacherService teacherService;
    private CourseService courseService;
    private EnrollmentStatsService enrollmentStatsService;
    private CourseStatsService courseStatsService;
    private AssignmentStatsService assignmentStatsService;
    private EnrollmentService enrollmentService;
    private AssignmentService assignmentService;
    private GradeService gradeService;

    @Override
    public void init() throws ServletException {
        teacherService = new TeacherService(new TeacherDAO(Teacher.class));
        courseService = new CourseService(new CourseDAO(Course.class));
        enrollmentStatsService =  new EnrollmentStatsService();
        courseStatsService =  new CourseStatsService();
        assignmentStatsService =  new AssignmentStatsService();
        enrollmentService = new EnrollmentService(new EnrollmentDAO(Enrollment.class));
        assignmentService = new AssignmentService(new AssignmentDAO(Assignment.class));
        gradeService = new GradeService(new GradeDAO(Grade.class));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                response.sendRedirect(request.getContextPath());
            } else {
                String[] pathParts = pathInfo.split("/");

                // Vérifie si l'URL est du type /teachers/{id}
                if (pathParts.length == 2) {
                    String teacherId = pathParts[1];
                    viewDashboard(request, response, teacherId);

                    // Vérifie si l'URL est du type /teachers/{id}/courses/{courseId}
                } else if (pathParts.length == 4 && "courses".equals(pathParts[2])) {
                    String teacherId = pathParts[1];
                    String courseId = pathParts[3];
                    viewCourseDetails(request, response, courseId);

                } else if (pathParts.length == 4 && "assignments".equals(pathParts[2])) {
                    String teacherId = pathParts[1];
                    String assignmentId = pathParts[3];
                    viewAssignmentDetails(request, response, assignmentId);

                }  else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL format");
                }
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }

    private void viewDashboard(HttpServletRequest request, HttpServletResponse response, String teacherId) throws ServletException, IOException {
        try {
            UUID id = UUID.fromString(teacherId);
            Teacher teacher = teacherService.getById(id);
            request.setAttribute("teacher", teacher);
            request.getRequestDispatcher("/WEB-INF/views/teachers/index.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching enrollments");
        }
    }

    private void viewCourseDetails(HttpServletRequest request, HttpServletResponse response, String courseId) throws ServletException, IOException {
        try {
            UUID cId = UUID.fromString(courseId);

            Course course = courseService.getById(cId);
            CompositeStats courseStats = courseStatsService.getStatsForCourse(course.getId());
            Map<Assignment, CompositeStats> assignmentStats = assignmentStatsService.getAssignmentsAndStatsForCourse(course.getId());
            Map<Enrollment, CompositeStats> enrollmentStats = enrollmentStatsService.getEnrollmentsAndStatsForCourse(course.getId());

            request.setAttribute("course", course);
            request.setAttribute("courseStats", courseStats);
            request.setAttribute("enrollmentStats", enrollmentStats);
            request.setAttribute("assignmentStats", assignmentStats);

            request.getRequestDispatcher("/WEB-INF/views/teachers/course-teacher.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request" + e);
        }
    }

    private void viewAssignmentDetails(HttpServletRequest request, HttpServletResponse response, String assignmentId) throws ServletException, IOException {
        try {
            UUID aId = UUID.fromString(assignmentId);

            Assignment assignment = assignmentService.getById(aId);
            CompositeStats assignmentStats = assignmentStatsService.getStatsForAssignment(assignment.getId());
            Map<Enrollment, Grade> enrollmentGrade = enrollmentService.getEnrollmentsAndGradesForAssignment(assignment.getId());
            request.setAttribute("assignment", assignment);
            request.setAttribute("assignmentStats", assignmentStats);
            request.setAttribute("enrollmentGrade", enrollmentGrade);

            request.getRequestDispatcher("/WEB-INF/views/teachers/assignment-teacher.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request" + e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");
        String action = request.getParameter("action");

        if ("PUT".equalsIgnoreCase(method)) {

        } else if ("DELETE".equalsIgnoreCase(method)) {
            if (Objects.equals(action, "deleteAssignment")) {
                deleteAssignment(request, response);
            }
        } else {
            if (Objects.equals(action, "createAssignment")) {
                createAssignment(request, response);
            }
        }
    }

    private void deleteAssignment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam != null) {
            UUID id = UUID.fromString(idParam);
            try {
                UUID courseId = assignmentService.getById(id).getCourse().getId();
                UUID teacherId = assignmentService.getById(id).getCourse().getTeacher().getId();
                assignmentService.delete(id);
                response.sendRedirect(request.getContextPath() + "/teachers/" + teacherId + "/courses/" + courseId);
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

            response.sendRedirect(request.getContextPath() + "/teachers/" + course.getTeacher().getId() + "/courses/" + course.getId());

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields for assignment creation");
        }
    }
}
