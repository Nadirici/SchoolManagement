package fr.cyu.schoolmanagementsystem.controller;

import fr.cyu.schoolmanagementsystem.controller.admin.AdminServlet;
import fr.cyu.schoolmanagementsystem.dao.*;
import fr.cyu.schoolmanagementsystem.entity.*;
import fr.cyu.schoolmanagementsystem.service.*;
import fr.cyu.schoolmanagementsystem.service.stats.AssignmentStatsService;
import fr.cyu.schoolmanagementsystem.service.stats.CourseStatsService;
import fr.cyu.schoolmanagementsystem.service.stats.EnrollmentStatsService;
import fr.cyu.schoolmanagementsystem.service.stats.StudentStatsService;
import fr.cyu.schoolmanagementsystem.util.AssignmentData;
import fr.cyu.schoolmanagementsystem.util.CompositeStats;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

@WebServlet("/students/*")
public class StudentServlet extends HttpServlet {

    private static StudentService studentService;
    private CourseService courseService;
    private EnrollmentStatsService enrollmentStatsService;
    private StudentStatsService studentStatsService;
    private CourseStatsService courseStatsService;
    private AssignmentStatsService assignmentStatsService;
    private EnrollmentService enrollmentService;
    private AssignmentService assignmentService;

    @Override
    public void init() throws ServletException {
        studentService = new StudentService(new StudentDAO(Student.class));
        courseService = new CourseService(new CourseDAO(Course.class));
        enrollmentStatsService =  new EnrollmentStatsService();
        studentStatsService =  new StudentStatsService();
        courseStatsService =  new CourseStatsService();
        assignmentStatsService =  new AssignmentStatsService();
        enrollmentService = new EnrollmentService(new EnrollmentDAO(Enrollment.class));
        assignmentService = new AssignmentService(new AssignmentDAO(Assignment.class));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Student student = checkStudentSession(request, response);

        if (student == null) {
            response.sendRedirect(request.getContextPath() + "/login?flashMessage=notAuthorized");
        } else {
            String pathInfo = request.getPathInfo();

            try {
                if (pathInfo == null || pathInfo.equals("/")) {
                    viewDashboard(request, response, student.getId());
                } else {
                    String[] pathParts = pathInfo.split("/");

                    // Vérifie si l'URL est du type /students
                    if (pathParts.length == 3 && "courses".equals(pathParts[1])) {
                        UUID courseId = UUID.fromString(pathParts[2]);
                        viewCourseDetails(request, response, student.getId(), courseId);

                    } else {
                        viewDashboard(request, response, student.getId());
                    }
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
            }
        }
    }

    private void viewDashboard(HttpServletRequest request, HttpServletResponse response, UUID studentId) throws ServletException, IOException {
        try {
            Student student = studentService.getById(studentId);
            List<Course> availableCourses = courseService.getAllNotEnroll(student.getId());
            CompositeStats studentStats = studentStatsService.getStatsForStudent(student.getId());
            Map<Enrollment, CompositeStats> enrollmentStats = enrollmentStatsService.getEnrollmentsAndStatsForStudent(student.getId());
            request.setAttribute("student", student);
            request.setAttribute("availableCourses", availableCourses);
            request.setAttribute("studentStats", studentStats);
            request.setAttribute("enrollmentStats", enrollmentStats);
            request.getRequestDispatcher("/WEB-INF/views/students/index.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching enrollments");
        }
    }

    private void viewCourseDetails(HttpServletRequest request, HttpServletResponse response, UUID studentId, UUID courseId) throws ServletException, IOException {
        try {
            Enrollment enrollment = enrollmentService.getByStudentAndCourse(studentId, courseId);
            CompositeStats enrollmentStats = enrollmentStatsService.getStatsForEnrollment(enrollment.getId());
            Map<Assignment, AssignmentData> combinedMap = new HashMap<>();

            Map<Assignment, Grade> assignmentGrade = assignmentService.getAssignmentsAndGradesForEnrollment(enrollment.getId());

            Map<UUID, Grade> assignmentGradeById = new HashMap<>();

            for (Map.Entry<Assignment, Grade> entry : assignmentGrade.entrySet()) {
                assignmentGradeById.put(entry.getKey().getId(), entry.getValue());
            }

            Map<Assignment, CompositeStats> assignmentStats = assignmentStatsService.getAssignmentsAndStatsForCourse(enrollment.getCourse().getId());

            for (Map.Entry<Assignment, CompositeStats> entry : assignmentStats.entrySet()) {
                Assignment assignment = entry.getKey();
                CompositeStats stats = entry.getValue();
                Grade grade = assignmentGradeById.get(assignment.getId());
                combinedMap.put(assignment, new AssignmentData(stats, grade));
            }

            request.setAttribute("enrollment", enrollment);
            request.setAttribute("enrollmentStats", enrollmentStats);
            request.setAttribute("assignmentsData", combinedMap);

            request.getRequestDispatcher("/WEB-INF/views/students/course-student.jsp").forward(request, response);
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
        } else {
            if (Objects.equals(action, "enroll")) {
                String studentId = request.getParameter("studentId");
                String courseId = request.getParameter("courseId");

                Enrollment newEnrollment = new Enrollment();
                newEnrollment.setStudent(studentService.getById(UUID.fromString(studentId)));
                newEnrollment.setCourse(courseService.getById(UUID.fromString(courseId)));

                enrollmentService.add(newEnrollment);
                response.sendRedirect(request.getContextPath() + "/students");
            }
        }
    }

    // Méthode statique pour vérifier la session et récupérer l'admin
    public static Student checkStudentSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Vérifier si l'utilisateur est authentifié
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("isAuthenticated") == null || !(boolean) session.getAttribute("isAuthenticated")) {
            return null;
        }

        // Vérifier si l'utilisateur est un administrateur
        String userType = (String) session.getAttribute("userType");
        if (!"student".equals(userType)) {

            return null;
        }

        // Si l'ID dans la session est un UUID, le convertir en UUID
        UUID sessionId = (UUID) session.getAttribute("userId");

        // Charger l'admin depuis la base de données
        Student student = studentService.getById(sessionId);
        if (student == null) {
            // Si l'admin n'existe pas, renvoyer une erreur
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Student not found");
            return null;
        }

        return student;  // Retourner l'admin si la session est valide
    }
}
