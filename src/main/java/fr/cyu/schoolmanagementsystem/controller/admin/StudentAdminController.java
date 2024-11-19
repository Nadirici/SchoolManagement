package fr.cyu.schoolmanagementsystem.controller.admin;

import fr.cyu.schoolmanagementsystem.dao.CourseDAO;
import fr.cyu.schoolmanagementsystem.dao.RegistrationRequestDAO;
import fr.cyu.schoolmanagementsystem.dao.StudentDAO;
import fr.cyu.schoolmanagementsystem.entity.Course;
import fr.cyu.schoolmanagementsystem.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.entity.RegistrationRequest;
import fr.cyu.schoolmanagementsystem.entity.Student;
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

@WebServlet("/students")
public class StudentAdminController extends HttpServlet {

    private StudentService studentService;
    private RequestService requestService;
    private CourseService courseService;
    private EnrollmentStatsService enrollmentStatsService;
    private StudentStatsService studentStatsService;

    @Override
    public void init() throws ServletException {
        studentService = new StudentService(new StudentDAO(Student.class));
        requestService = new RequestService(new RegistrationRequestDAO(RegistrationRequest.class));
        courseService = new CourseService(new CourseDAO(Course.class));
        enrollmentStatsService =  new EnrollmentStatsService();
        studentStatsService =  new StudentStatsService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam != null) {
            viewStudent(request, response);
        } else {
            listStudents(request, response);
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

    private void viewStudent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UUID id = UUID.fromString(request.getParameter("id"));
        try {
            Student student = studentService.getById(id);
            List<Course> availableCourses = courseService.getAllNotEnroll(student.getId());
            CompositeStats studentStats = studentStatsService.getStatsForStudent(student.getId());
            Map<Enrollment, CompositeStats> enrollmentStatsMap = enrollmentStatsService.getEnrollmentsStatsMapForStudent(student.getId());

            request.setAttribute("student", student);
            request.setAttribute("availableCourses", availableCourses);
            request.setAttribute("studentStats", studentStats);
            request.setAttribute("enrollmentStats", enrollmentStatsMap);

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
        } else {
            handleAddStudent(request, response);
        }
        response.sendRedirect(request.getContextPath() + "/students");
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

        Student student = new Student(firstname, lastname, dateOfBirth, email, hashedPassword, salt, false);
        UUID id = studentService.add(student);

        RegistrationRequest registrationRequest = new RegistrationRequest(student);
        requestService.add(registrationRequest);
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
