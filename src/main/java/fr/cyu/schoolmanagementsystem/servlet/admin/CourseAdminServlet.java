package fr.cyu.schoolmanagementsystem.servlet.admin;

import fr.cyu.schoolmanagementsystem.servlet.Routes;
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
import fr.cyu.schoolmanagementsystem.util.Gmailer;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet(Routes.ADMIN_COURSES + "/*")
public class CourseAdminServlet extends HttpServlet {

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

        Admin admin = AdminServlet.checkAdminSession(request, response);

        if (admin != null) {
            // Ajouter l'admin en tant qu'attribut de la requête
            request.setAttribute("admin", admin);
        } else {
            response.sendRedirect(request.getContextPath() + "/login?flashMessage=notAuthorized");
        }

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
            response.sendRedirect(request.getContextPath() + Routes.ADMIN_COURSES);
        } else {
            try {
                createCourse(request, response);

            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateCourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseId = request.getParameter("id");
        String courseName = request.getParameter("name");
        String courseDescription = request.getParameter("description");
        String teacherId = request.getParameter("teacherId");
        String newDay = request.getParameter("day");
        String newStartTime = request.getParameter("startTime");
        String newEndTime = request.getParameter("endTime");

        if (courseId != null && !courseId.isEmpty()) {
            UUID id = UUID.fromString(courseId);

            try {
                Course course = courseService.getById(id);


                // Mettre à jour le nom et la description immédiatement
                course.setName(courseName);
                course.setDescription(courseDescription);
                courseService.update(course); // Sauvegarder le nom et la description

                // Convertir le jour (String) en DayOfWeek
                DayOfWeek dayOfWeekEnum = DayOfWeek.valueOf(newDay.toUpperCase()); // Assure-toi que le jour est en majuscule

                // Récupérer l'enseignant et vérifier sa disponibilité
                Teacher teacher = teacherService.getById(UUID.fromString(teacherId));
                Course tempCourse = new Course();
                tempCourse.setId(id);
                tempCourse.setDayOfWeek(dayOfWeekEnum);
                tempCourse.setStartTime(LocalTime.parse(newStartTime));
                tempCourse.setEndTime(LocalTime.parse(newEndTime));

                // Vérifier la disponibilité de l'enseignant
                if (!teacherService.isAvailable(teacher, tempCourse)) {

                    response.sendRedirect(request.getContextPath() + Routes.ADMIN_COURSES + "/"+tempCourse.getId()+ "?flashMessage=teacherNotAvailable");
                    return;

                }

                for (Enrollment enrollment : course.getEnrollments()) {
                    Student student = enrollment.getStudent(); // Récupère l'étudiant depuis l'inscription
                    if (!studentService.isStudentAvailable(student, tempCourse)) {
                        response.sendRedirect(request.getContextPath() + Routes.ADMIN_COURSES + "/"+tempCourse.getId()+ "?flashMessage=studentNotAvailable");
                        return;

                    }
                }

                // Mettre à jour le cours si les vérifications sont passées
                course.setName(courseName);
                course.setDescription(courseDescription);
                course.setTeacher(teacher);
                course.setDayOfWeek(dayOfWeekEnum); // Mettre à jour le jour
                course.setStartTime(LocalTime.parse(newStartTime)); // Mettre à jour l'heure de début
                course.setEndTime(LocalTime.parse(newEndTime)); // Mettre à jour l'heure de fin

                courseService.update(course);
                response.sendRedirect(request.getContextPath() + Routes.ADMIN_COURSES + "/" + course.getId());

            } catch (EntityNotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Course not found");
            } catch (IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Jour de la semaine invalide");
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



    private void createCourse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, GeneralSecurityException, MessagingException {
        String courseName = request.getParameter("name");
        String courseDescription = request.getParameter("description");
        String teacherId = request.getParameter("teacherId");
        String dayOfWeek = request.getParameter("dayOfWeek");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        if (courseName != null && !courseName.isEmpty() &&
                courseDescription != null && !courseDescription.isEmpty() &&
                teacherId != null && !teacherId.isEmpty() &&
                dayOfWeek != null && !dayOfWeek.isEmpty() &&
                startTime != null && !startTime.isEmpty() &&
                endTime != null && !endTime.isEmpty()) {

            Teacher teacher = teacherService.getById(UUID.fromString(teacherId));

            // Préparer le cours pour la vérification de disponibilité
            Course newCourse = new Course();
            newCourse.setDayOfWeek(DayOfWeek.valueOf(dayOfWeek.toUpperCase()));
            newCourse.setStartTime(LocalTime.parse(startTime));
            newCourse.setEndTime(LocalTime.parse(endTime));

            // Vérification de disponibilité du professeur
            if (!teacherService.isAvailable(teacher, newCourse)) {
                response.sendRedirect(request.getContextPath() + Routes.ADMIN_COURSES + "?flashMessage=notAvailable");
                return;
            }

            // Création du cours
            newCourse.setName(courseName);
            newCourse.setDescription(courseDescription);
            newCourse.setTeacher(teacher);
            courseService.add(newCourse);

            // Notification par email
            String link = "http://localhost:8080/SchoolManagement_war_exploded/login";
            Gmailer gmailer = new Gmailer();
            gmailer.sendMail(
                    "Nouvelle affectation à un cours",
                    "Bonjour " + teacher.getFirstname() + " " + teacher.getLastname() + ",<br><br>" +
                            "Vous avez été affecté à l'enseignement d'un nouveau cours.<br><br>" +
                            "Voici les détails :<br>" +
                            "- Cours : " + courseName + "<br>" +
                            "- Description : " + courseDescription + "<br>" +
                            "- Jour : " + dayOfWeek + "<br>" +
                            "- Heure : de " + startTime + " à " + endTime + "<br><br>" +
                            "Pour consulter vos cours et plus de détails, connectez-vous à votre espace enseignant :<br>" +
                            "<a href='" + link + "'>Voir mes cours</a><br><br>" +
                            "Cordialement,<br>" +
                            "L'équipe de gestion du système.",
                    teacher.getEmail()
            );

            response.sendRedirect(request.getContextPath() + Routes.ADMIN_COURSES);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields for course creation");
        }
    }



}
