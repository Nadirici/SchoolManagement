package fr.cyu.schoolmanagementsystem.controller.admin;

import fr.cyu.schoolmanagementsystem.dao.CourseDAO;
import fr.cyu.schoolmanagementsystem.dao.EnrollmentDAO;
import fr.cyu.schoolmanagementsystem.dao.GradeDAO;
import fr.cyu.schoolmanagementsystem.dao.StudentDAO;
import fr.cyu.schoolmanagementsystem.entity.Course;
import fr.cyu.schoolmanagementsystem.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.entity.Grade;
import fr.cyu.schoolmanagementsystem.entity.Student;
import fr.cyu.schoolmanagementsystem.service.CourseService;
import fr.cyu.schoolmanagementsystem.service.EnrollmentService;
import fr.cyu.schoolmanagementsystem.service.StudentService;
import fr.cyu.schoolmanagementsystem.service.GradeService;
import fr.cyu.schoolmanagementsystem.service.stats.EnrollmentStatsService;
import fr.cyu.schoolmanagementsystem.service.stats.StudentStatsService;
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
@WebServlet("/student/*")
public class StudentDashboardServlet extends HttpServlet {

    private final StudentService studentService;
    private final EnrollmentService enrollmentService;
    private final CourseService courseService;
    private final GradeService gradeService;

    public StudentDashboardServlet() {
        this.studentService = new StudentService(
                new StudentDAO(Student.class),
                new EnrollmentService(new EnrollmentDAO(Enrollment.class)),
                new GradeService(new GradeDAO(Grade.class))
        );
        this.enrollmentService = new EnrollmentService(new EnrollmentDAO(Enrollment.class));
        this.courseService = new CourseService(new CourseDAO(Course.class));
        this.gradeService = new GradeService(new GradeDAO(Grade.class));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Récupère l'ID de l'utilisateur dans la session
        UUID sessionStudentId = (UUID) request.getSession().getAttribute("userId");

        String pathInfo = request.getPathInfo(); // Récupère le {id} dans l'URL
        System.out.println("Path Info: " + pathInfo);  // Log pour vérifier l'URL reçue

        if (pathInfo != null && pathInfo.startsWith("/")) {
            pathInfo = pathInfo.substring(1); // Supprime le premier "/"
        }

        try {
            if (pathInfo != null) {
                System.out.println("Extracted student ID: " + pathInfo);  // Log pour vérifier l'ID extrait
                UUID studentId = UUID.fromString(pathInfo); // Convertit le {id} en UUID

                request.getSession().setAttribute("userId", studentId); // où studentId est de type UUID

                // Vérification que l'ID de l'URL correspond à celui dans la session
                if (!studentId.equals(sessionStudentId)) {
                    // Si les IDs ne correspondent pas, redirige vers une page d'erreur
                    System.out.println("Access denied: The student ID in the URL does not match the logged-in student.");
                    response.sendRedirect(request.getContextPath() + "/errorServlet?flashMessage=accessDenied");
                    return;
                }

                // Log avant d'appeler le service
                System.out.println("Searching student with ID: " + studentId);
                Student student = studentService.getById(studentId);

                if (student != null) {
                    // Log pour vérifier que l'étudiant est trouvé
                    System.out.println("Found student: " + student.getFirstname() + " " + student.getLastname());

                    // Récupère les cours auxquels l'étudiant est inscrit
                    List<Enrollment> enrolledCourses = enrollmentService.getEnrollmentsForStudent(studentId);
                    System.out.println("Enrolled courses: " + enrolledCourses.size());

                    // Récupère les cours disponibles pour inscription
                    List<Course> availableCourses = courseService.getAllNotEnroll(studentId);
                    System.out.println("Available courses: " + availableCourses.size());

                    // Récupère les statistiques de chaque cours de l'étudiant
                    Map<Course, StudentService.CourseStats> courseStatsMap = studentService.getCourseStats(studentId);
                    System.out.println("Course stats map size: " + courseStatsMap.size());




                    Map<Enrollment,CompositeStats> enrollmentCompositeStatsMap = new HashMap<>() ;


                    // calcul de moyenne pour chaque inscription
                    EnrollmentStatsService enrollmentStatsService = new EnrollmentStatsService();

                    for (Enrollment enrollment : enrolledCourses) {
                        System.out.println("Enrollment ID: " + enrollment.getId());
                        CompositeStats stats = enrollmentStatsService.getStatsForEnrollment(enrollment.getId());
                        enrollmentCompositeStatsMap.put(enrollment, stats);
                    }

                    StudentStatsService studentStatsService = new StudentStatsService();
                    CompositeStats studentStats = studentStatsService.getStatsForStudent(studentId);
                    double overallAverage = studentStats.getAverage();
                    System.out.println("Moyenne générale de l'étudiant : " + overallAverage);
                    // Passer les données à la JSP via le dispatcher
                    request.setAttribute("student", student);
                    request.setAttribute("enrolledCourses", enrolledCourses);
                    request.setAttribute("availableCourses", availableCourses);
                    request.setAttribute("courseStats", enrollmentCompositeStatsMap);
                    request.setAttribute("overallAverage", overallAverage);

                    // Log avant de rediriger vers la JSP
                    System.out.println("Forwarding to the dashboard JSP");

                    // Redirection vers le servlet qui gère le rendu de la JSP
                    request.getRequestDispatcher("/WEB-INF/views/admin/students/dashboard.jsp").forward(request, response);

                } else {
                    // Si l'étudiant n'est pas trouvé, redirection vers une page d'erreur
                    System.out.println("Student not found with ID: " + studentId);
                    response.sendRedirect(request.getContextPath() + "/errorServlet?flashMessage=studentNotFound");
                }
            } else {
                // Si l'ID est manquant dans l'URL, redirection vers une page d'erreur
                System.out.println("Missing student ID in the URL");
                response.sendRedirect(request.getContextPath() + "/errorServlet?flashMessage=missingStudentId");
            }
        } catch (IllegalArgumentException e) {
            // Si l'ID est invalide, redirection vers une page d'erreur
            System.out.println("Invalid UUID format in the URL: " + pathInfo);
            response.sendRedirect(request.getContextPath() + "/errorServlet?flashMessage=invalidStudentId");
        }
    }
}
