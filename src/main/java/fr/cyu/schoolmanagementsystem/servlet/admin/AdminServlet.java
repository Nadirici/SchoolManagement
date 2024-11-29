package fr.cyu.schoolmanagementsystem.servlet.admin;

import fr.cyu.schoolmanagementsystem.dao.*;
import fr.cyu.schoolmanagementsystem.entity.*;
import fr.cyu.schoolmanagementsystem.entity.enumeration.Department;
import fr.cyu.schoolmanagementsystem.service.*;
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
import java.util.stream.Collectors;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {

    private static AdminService adminService;
    private StudentService studentService;
    private TeacherService teacherService;
    private CourseService courseService;
    private CourseStatsService courseStatsService;
    private EnrollmentService enrollmentService;
    private EnrollmentStatsService enrollmentStatsService;
    private RequestService requestService;

    @Override
    public void init() throws ServletException {
        adminService = new AdminService(new AdminDAO(Admin.class));
        studentService = new StudentService(new StudentDAO(Student.class));
        teacherService = new TeacherService(new TeacherDAO(Teacher.class));
        courseService = new CourseService(new CourseDAO(Course.class));
        courseStatsService = new CourseStatsService();
        enrollmentService = new EnrollmentService(new EnrollmentDAO(Enrollment.class));
        enrollmentStatsService = new EnrollmentStatsService();
        requestService = new RequestService(new RegistrationRequestDAO(RegistrationRequest.class));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Admin admin = checkAdminSession(request, response);

        if (admin != null) {
            // Ajouter l'admin en tant qu'attribut de la requête
            int nbrVerifiedStudents = studentService.getAllVerified().size();
            int nbrVerifiedTeachers = teacherService.getAllVerified().size();
            int nbrRequestsPending = requestService.getAllPending().size();

            List<Course> allCourses = courseService.getAll();
            Map<UUID, Double> courseAverageGrades = allCourses.stream()
                            .collect(Collectors.toMap(
                                    Course::getId,
                                    course -> courseStatsService.getStatsForCourse(course.getId()).getAverage()
                            ));

            Map<Department, Double> passingStudentsByDepartment = new HashMap<>();
            Map<UUID, Double> passingStudentsByCourse = new HashMap<>();
            Map<Department, List<Course>> coursesByDepartment = new HashMap<>();
            Set<UUID> totalPassingStudents = new HashSet<>();

            for (Department department : Department.values()) {
                System.out.println(department.getName());
                List<Course> coursesInDepartment = courseService.getByDepartment(department.getName());
                double totalStudentsInDepartment = 0;
                double passingStudents = 0;

                for (Course course : coursesInDepartment) {
                    System.out.println(course.getName());
                    List<Enrollment> enrollments = enrollmentService.getEnrollmentsForCourse(course.getId());
                    if (enrollments.isEmpty()) {
                        continue; // Ignore courses with no enrollments (no grades)
                    }

                    double passingStudentsInCourse = 0;
                    for (Enrollment enrollment : enrollments) {
                        System.out.println(enrollment.getStudent().getLastname());
                        double averageGrade = enrollmentStatsService.getStatsForEnrollment(enrollment.getId()).getAverage();
                        if (averageGrade >= 10.0) {
                            totalPassingStudents.add(enrollment.getStudent().getId());
                            passingStudentsInCourse++;
                        } else {
                            totalPassingStudents.remove(enrollment.getStudent().getId());
                        }
                    }
                    passingStudents += passingStudentsInCourse;

                    totalStudentsInDepartment += enrollments.size();
                    double percentageCourse = !enrollments.isEmpty() ? (passingStudentsInCourse / enrollments.size()) * 100 : 0;
                    passingStudentsByCourse.put(course.getId(), percentageCourse);

                }

                double percentageDepartment = totalStudentsInDepartment > 0 ? (passingStudents / totalStudentsInDepartment) * 100 : 0;

                passingStudentsByDepartment.put(department, percentageDepartment);
                coursesByDepartment.put(department, coursesInDepartment);
            }

            request.setAttribute("nbrVerifiedStudents", nbrVerifiedStudents);
            request.setAttribute("nbrVerifiedTeachers", nbrVerifiedTeachers);
            request.setAttribute("coursesByDepartment", coursesByDepartment);
            request.setAttribute("passingStudentsByDepartment", passingStudentsByDepartment);
            request.setAttribute("passingStudentsByCourse", passingStudentsByCourse);
            request.setAttribute("averageGradesByCourse", courseAverageGrades);
            request.setAttribute("nbrRequestsPending", nbrRequestsPending);
            request.setAttribute("totalPassingStudents", nbrVerifiedStudents > 0 ? (totalPassingStudents.size()/nbrVerifiedStudents)*100 : 0);
            request.setAttribute("admin", admin);

            request.getRequestDispatcher("/WEB-INF/views/admin/index.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login?flashMessage=notAuthorized");
        }

    }

    // Méthode statique pour vérifier la session et récupérer l'admin
    public static Admin checkAdminSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Vérifier si l'utilisateur est authentifié
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("isAuthenticated") == null || !(boolean) session.getAttribute("isAuthenticated")) {
            return null;
        }

        // Vérifier si l'utilisateur est un administrateur
        String userType = (String) session.getAttribute("userType");
        if (!"admin".equals(userType)) {

            return null;
        }

        // Si l'ID dans la session est un UUID, le convertir en UUID
        UUID sessionId = (UUID) session.getAttribute("userId");

        // Charger l'admin depuis la base de données
        Admin admin = adminService.getById(sessionId);
        if (admin == null) {
            // Si l'admin n'existe pas, renvoyer une erreur
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Admin not found");
            return null;
        }

        return admin;  // Retourner l'admin si la session est valide
    }
}
