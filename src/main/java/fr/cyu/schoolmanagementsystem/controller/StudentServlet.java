package fr.cyu.schoolmanagementsystem.controller;

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

import java.io.IOException;
import java.util.*;

@WebServlet(Routes.STUDENT + "/*")
public class StudentServlet extends HttpServlet {

    private StudentService studentService;
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
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                response.sendRedirect(request.getContextPath());
            } else {
                String[] pathParts = pathInfo.split("/");

                // Vérifie si l'URL est du type /students/{id}
                if (pathParts.length == 2) {
                    String studentId = pathParts[1];
                    viewDashboard(request, response, studentId);

                    // Vérifie si l'URL est du type /students/{id}/courses/{courseId}
                } else if (pathParts.length == 4 && "courses".equals(pathParts[2])) {
                    String studentId = pathParts[1];
                    String courseId = pathParts[3];
                    System.out.println(studentId + " " + courseId);
                    viewCourseDetails(request, response, studentId, courseId);

                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL format");
                }
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }

    private void viewDashboard(HttpServletRequest request, HttpServletResponse response, String studentId) throws ServletException, IOException {
        try {
            UUID id = UUID.fromString(studentId);
            Student student = studentService.getById(id);
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

    private void viewCourseDetails(HttpServletRequest request, HttpServletResponse response, String studentId, String courseId) throws ServletException, IOException {
        try {
            UUID sId = UUID.fromString(studentId);
            UUID cId = UUID.fromString(courseId);
            Enrollment enrollment = enrollmentService.getByStudentAndCourse(sId, cId);
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
                response.sendRedirect(request.getContextPath() + "/students/" + studentId);
            }
        }
    }
}
