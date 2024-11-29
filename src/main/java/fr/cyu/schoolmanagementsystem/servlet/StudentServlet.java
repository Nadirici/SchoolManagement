package fr.cyu.schoolmanagementsystem.servlet;

import fr.cyu.schoolmanagementsystem.dao.*;
import fr.cyu.schoolmanagementsystem.entity.*;
import fr.cyu.schoolmanagementsystem.service.*;
import fr.cyu.schoolmanagementsystem.service.stats.AssignmentStatsService;
import fr.cyu.schoolmanagementsystem.service.stats.CourseStatsService;
import fr.cyu.schoolmanagementsystem.service.stats.EnrollmentStatsService;
import fr.cyu.schoolmanagementsystem.service.stats.StudentStatsService;
import fr.cyu.schoolmanagementsystem.util.AssignmentData;
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
    private PdfService pdfService;

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
        pdfService = new PdfService();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Vérifier si l'étudiant est connecté
        Student student = checkStudentSession(request, response);

        // Si l'étudiant n'est pas connecté, rediriger vers la page de connexion
        if (student == null) {
            response.sendRedirect(request.getContextPath() + "/login?flashMessage=notAuthorized");
        } else {
            String pathInfo = request.getPathInfo();

            try {
                if (pathInfo == null || pathInfo.equals("/")) {
                    // Si l'URL est "/students", afficher le tableau de bord de l'étudiant
                    viewDashboard(request, response, student.getId());
                } else {
                    // Découper l'URL en parties
                    String[] pathParts = pathInfo.split("/");

                    // Si l'URL est "/students/courses", afficher la liste des cours
                    if (pathParts.length == 2 && "courses".equals(pathParts[1])) {
                        viewCourseList(request, response, student.getId());
                    }
                    // Si l'URL est "/students/courses/{courseId}", afficher les détails d'un cours spécifique
                    else if (pathParts.length == 3 && "courses".equals(pathParts[1])) {
                        UUID courseId = UUID.fromString(pathParts[2]);
                        viewCourseDetails(request, response, student.getId(), courseId);
                    }
                    // Si l'URL est "/students/report/pdf", générer le rapport PDF
                    else if (pathParts.length == 3 && "report".equals(pathParts[1]) && "pdf".equals(pathParts[2])) {
                        generatePdfReport(student.getId(), response);
                    }
                    // Si l'URL est "/students/schedule", afficher l'emploi du temps
                    else if (pathParts.length == 2 && "schedule".equals(pathParts[1])) {
                        viewStudentSchedule(request, response, student.getId());
                    }
                    // Sinon, afficher le tableau de bord par défaut
                    else {
                        viewDashboard(request, response, student.getId());
                    }
                }
            } catch (Exception e) {
                // En cas d'erreur, renvoyer une erreur 500
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
            }
        }
    }

    private void viewStudentSchedule(HttpServletRequest request, HttpServletResponse response, UUID studentId) throws ServletException, IOException {
        try {
            Map<String, List<Course>> studentSchedule = getStudentSchedule(studentId);

            // Créer la liste des heures de la journée (exemple de 8h à 18h)
            List<String> hoursOfDay = Arrays.asList("08:30", "10:00","10:15", "11:45", "13:00", "14:30","14:45", "16:15", "16:30",  "18:00",  "18:15","19:30");
            request.setAttribute("hoursOfDay", hoursOfDay);
            request.setAttribute("studentSchedule", studentSchedule);
            request.getRequestDispatcher("/WEB-INF/views/students/schedule.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching student schedule");
        }
    }

    private Map<String, List<Course>> getStudentSchedule(UUID studentId) throws Exception {
        // Récupérer les inscriptions de l'étudiant
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsForStudent(studentId);

        // Organiser les cours par jour
        Map<String, List<Course>> schedule = new TreeMap<>();

        for (Enrollment enrollment : enrollments) {
            Course course = enrollment.getCourse();
            String dayOfWeek = course.getFrenchDayOfWeek(); // suppose que le cours a un champ "schedule" qui contient le jour

            schedule.computeIfAbsent(dayOfWeek, k -> new ArrayList<>()).add(course);
        }

        return schedule;
    }



    private void viewCourseList(HttpServletRequest request, HttpServletResponse response, UUID studentId) throws ServletException, IOException {
        try {
            Student student = studentService.getById(studentId);
            List<Course> availableCourses = courseService.getAllNotEnroll(student.getId());
            CompositeStats studentStats = studentStatsService.getStatsForStudent(student.getId());
            Map<Enrollment, CompositeStats> enrollmentStats = enrollmentStatsService.getEnrollmentsAndStatsForStudent(student.getId());
            request.setAttribute("student", student);
            request.setAttribute("availableCourses", availableCourses);
            request.setAttribute("studentStats", studentStats);
            request.setAttribute("enrollmentStats", enrollmentStats);
            request.getRequestDispatcher("/WEB-INF/views/students/courses.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching enrollments");
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
            // Code pour la méthode PUT si nécessaire
        } else if ("DELETE".equalsIgnoreCase(method)) {
            // Code pour la méthode DELETE si nécessaire
        } else {
            if (Objects.equals(action, "enroll")) {
                String studentId = request.getParameter("studentId");
                String courseId = request.getParameter("courseId");

                Student student = studentService.getById(UUID.fromString(studentId));
                Course course = courseService.getById(UUID.fromString(courseId));

                // Vérification si l'étudiant est disponible pour ce cours
                if (studentService.isStudentAvailable(student, course)) {
                    // L'étudiant est disponible, on procède à l'inscription
                    Enrollment newEnrollment = new Enrollment();
                    newEnrollment.setStudent(student);
                    newEnrollment.setCourse(course);

                    enrollmentService.add(newEnrollment);
                    response.sendRedirect(request.getContextPath() + "/students");
                } else {
                    // L'étudiant n'est pas disponible, rediriger avec un message d'erreur
                    request.getSession().setAttribute("flashMessage", "notAvailable");
                    response.sendRedirect(request.getContextPath() + "/students");
                }
            }
        }
    }


    public void generatePdfReport(UUID studentId, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Récupérer les informations de l'étudiant
            Student student = studentService.getById(studentId);
            if (student == null) {
                throw new IllegalArgumentException("Étudiant introuvable avec l'ID " + studentId);
            }

            // Récupérer les inscriptions de l'étudiant
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsForStudent(studentId);
            if (enrollments.isEmpty()) {
                throw new IllegalArgumentException("Aucun cours trouvé pour l'étudiant " + student.getFirstname());
            }

            // Préparer les statistiques et données pour le rapport
            Map<UUID, String> courseAverages = new HashMap<>();
            Map<UUID, Double> courseMinAverages = new HashMap<>();
            Map<UUID, Double> courseMaxAverages = new HashMap<>();

            for (Enrollment enrollment : enrollments) {
                UUID courseId = enrollment.getCourse().getId();

                CompositeStats courseStats = courseStatsService.getStatsForCourse(courseId);
                CompositeStats enrollmentStats = enrollmentStatsService.getStatsForEnrollment(enrollment.getId());

                // Stocker les moyennes calculées
                double studentAverage = enrollmentStats.getAverage();
                double minAverage = courseStats.getMin();
                double maxAverage = courseStats.getMax();

                courseAverages.put(courseId, Double.isNaN(studentAverage) ? "Pas de note" : String.format("%.2f", studentAverage));
                courseMinAverages.put(courseId, minAverage);
                courseMaxAverages.put(courseId, maxAverage);
            }

            // Calculer la moyenne générale de l'étudiant
            CompositeStats studentStats = studentStatsService.getStatsForStudent(studentId);
            double studentGlobalAverage = studentStats.getAverage();

            // Générer le PDF
            byte[] pdfContent = pdfService.createStudentReport(
                    student,
                    enrollments.stream().map(Enrollment::getCourse).collect(Collectors.toList()),
                    courseAverages,
                    courseMinAverages,
                    courseMaxAverages,
                    studentGlobalAverage
            );

            // Configurer la réponse HTTP avec le PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"Bulletin-" + student.getFirstname() + ".pdf\"");
            response.getOutputStream().write(pdfContent);

        } catch (IllegalArgumentException e) {
            // Gérer les erreurs spécifiques
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            // Gérer les erreurs générales
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur interne du serveur.");
            e.printStackTrace();
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
