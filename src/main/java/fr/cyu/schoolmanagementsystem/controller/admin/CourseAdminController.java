package fr.cyu.schoolmanagementsystem.controller.admin;

import fr.cyu.schoolmanagementsystem.controller.Routes;
import fr.cyu.schoolmanagementsystem.dao.CourseDAO;
import fr.cyu.schoolmanagementsystem.dao.StudentDAO;
import fr.cyu.schoolmanagementsystem.dao.TeacherDAO;
import fr.cyu.schoolmanagementsystem.entity.*;
import fr.cyu.schoolmanagementsystem.service.CourseService;
import fr.cyu.schoolmanagementsystem.service.StudentService;
import fr.cyu.schoolmanagementsystem.service.TeacherService;
import fr.cyu.schoolmanagementsystem.service.stats.AssignmentStatsService;
import fr.cyu.schoolmanagementsystem.service.stats.CourseStatsService;
import fr.cyu.schoolmanagementsystem.service.stats.EnrollmentStatsService;
import fr.cyu.schoolmanagementsystem.util.CompositeStats;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet(Routes.ADMIN_COURSES)
public class CourseAdminController extends HttpServlet {

    private CourseService courseService;
    private TeacherService teacherService;
    private StudentService studentService;
    private AssignmentStatsService assignmentStatsService;
    private CourseStatsService courseStatsService;
    private EnrollmentStatsService enrollmentStatsService;

    @Override
    public void init() throws ServletException {
        courseService = new CourseService(new CourseDAO(Course.class));
        teacherService = new TeacherService(new TeacherDAO(Teacher.class));
        studentService = new StudentService(new StudentDAO(Student.class));
        assignmentStatsService = new AssignmentStatsService();
        courseStatsService = new CourseStatsService();
        enrollmentStatsService = new EnrollmentStatsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                listCourses(request, response);
            } else {
                String[] pathParts = pathInfo.split("/");

                if (pathParts.length == 2) {
                    String idSegment = pathParts[1];
                    viewCourse(request, response, idSegment);
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid URL format");
                }
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }

    private void listCourses(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Course> courses = courseService.getAll();
            List<Teacher> availableTeachers = teacherService.getAllVerified();

            request.setAttribute("courses", courses);
            request.setAttribute("availableTeachers", availableTeachers);

            request.getRequestDispatcher("/WEB-INF/views/admin/courses/courses.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching courses");
        }
    }

    private void viewCourse(HttpServletRequest request, HttpServletResponse response, String idSegment) throws ServletException, IOException {
        try {
            UUID id = UUID.fromString(idSegment);
            Course course = courseService.getById(id);
            CompositeStats courseStats = courseStatsService.getStatsForCourse(course.getId());
            Map<Assignment, CompositeStats> assignmentStats = assignmentStatsService.getAssignmentsAndStatsForCourse(id);
            Map<Enrollment, CompositeStats> enrollmentStats = enrollmentStatsService.getEnrollmentsAndStatsForCourse(id);
            List<Student> availableStudents = studentService.getAllStudentsNotEnrollInCourse(course.getId());
            List<Teacher> availableTeachers = teacherService.getAllVerified();

            request.setAttribute("course", course);
            request.setAttribute("courseStats", courseStats);
            request.setAttribute("enrollmentStats", enrollmentStats);
            request.setAttribute("assignmentStats", assignmentStats);
            request.setAttribute("availableStudents", availableStudents);
            request.setAttribute("availableTeachers", availableTeachers);

            request.getRequestDispatcher("/WEB-INF/views/admin/courses/course-details.jsp").forward(request, response);
        } catch (EntityNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Course not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("_method");

        if ("PUT".equalsIgnoreCase(method)) {
            updateCourse(request, response);
        } else if ("DELETE".equalsIgnoreCase(method)) {
            deleteCourse(request, response);
        } else {
            createCourse(request, response);
        }
        response.sendRedirect(request.getContextPath() + Routes.ADMIN_COURSES);
    }

    private void updateCourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseId = request.getParameter("id");
        String courseName = request.getParameter("name");
        String courseDescription = request.getParameter("description");
        String teacherId = request.getParameter("teacherId");

        if (courseId != null && !courseId.isEmpty()) {
            UUID id = UUID.fromString(courseId);

            try {
                Course course = courseService.getById(id);
                course.setName(courseName);
                course.setDescription(courseDescription);
                System.out.println(teacherId);
                course.setTeacher(teacherService.getById(UUID.fromString(teacherId)));

                courseService.update(course);

            } catch (EntityNotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Course not found");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course ID is required for update");
        }
    }

    private void deleteCourse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String courseId = request.getParameter("id");
        if (courseId != null && !courseId.isEmpty()) {
            UUID id = UUID.fromString(courseId);
            try {
                courseService.delete(id);
            } catch (EntityNotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Course not found");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course ID is required for deletion");
        }
    }

    private void createCourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseName = request.getParameter("name");
        String courseDescription = request.getParameter("description");
        String teacherId = request.getParameter("teacherId");

        if (courseName != null && !courseName.isEmpty() && courseDescription != null && !courseDescription.isEmpty() && teacherId != null) {
            Course course = new Course();
            course.setName(courseName);
            course.setDescription(courseDescription);
            Teacher teacher = teacherService.getById(UUID.fromString(teacherId));
            course.setTeacher(teacher);

            courseService.add(course);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields for course creation");
        }
    }
}
