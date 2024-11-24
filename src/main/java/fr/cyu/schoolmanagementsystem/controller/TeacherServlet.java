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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

@WebServlet(Routes.TEACHER + "/*")
public class TeacherServlet extends HttpServlet {

    private static TeacherService teacherService;
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

        Teacher teacher = checkTeacherSession(request, response);

        if (teacher == null) {
            response.sendRedirect(request.getContextPath() + "/login?flashMessage=notAuthorized");
        } else {
            String pathInfo = request.getPathInfo();

            try {
                if (pathInfo == null || pathInfo.equals("/")) {
                    viewDashboard(request, response, teacher.getId());
                } else {
                    String[] pathParts = pathInfo.split("/");

                    if (pathParts.length == 3 && "courses".equals(pathParts[1])) {
                        String courseId = pathParts[2];
                        viewCourseDetails(request, response, courseId);

                    } else if (pathParts.length == 3 && "assignments".equals(pathParts[1])) {
                        String assignmentId = pathParts[2];
                        viewAssignmentDetails(request, response, assignmentId);

                    }  else {
                        viewDashboard(request, response, teacher.getId());
                    }
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
            }
        }


    }

    private void viewDashboard(HttpServletRequest request, HttpServletResponse response, UUID teacherId) throws ServletException, IOException {
        try {
            Teacher teacher = teacherService.getById(teacherId);
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
            if ("saveGrades".equalsIgnoreCase(action)) {
                saveGrades(request, response);
            }
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
                response.sendRedirect(request.getContextPath() + "/teachers/courses/" + courseId);
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

            response.sendRedirect(request.getContextPath() + "/teachers/courses/" + course.getId());

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
        response.sendRedirect(request.getContextPath() + "/teachers/courses/" + request.getParameter("courseId"));
    }

    // Méthode statique pour vérifier la session et récupérer l'admin
    public static Teacher checkTeacherSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Vérifier si l'utilisateur est authentifié
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("isAuthenticated") == null || !(boolean) session.getAttribute("isAuthenticated")) {
            return null;
        }

        // Vérifier si l'utilisateur est un administrateur
        String userType = (String) session.getAttribute("userType");
        if (!"teacher".equals(userType)) {

            return null;
        }

        // Si l'ID dans la session est un UUID, le convertir en UUID
        UUID sessionId = (UUID) session.getAttribute("userId");

        // Charger l'admin depuis la base de données
        Teacher teacher = teacherService.getById(sessionId);
        if (teacher == null) {
            // Si l'admin n'existe pas, renvoyer une erreur
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Teacher not found");
            return null;
        }

        return teacher;  // Retourner l'admin si la session est valide
    }
}
