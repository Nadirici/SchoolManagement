package fr.cyu.schoolmanagementsystem.controller.web;

import fr.cyu.schoolmanagementsystem.model.dto.*;
import fr.cyu.schoolmanagementsystem.model.entity.*;
import fr.cyu.schoolmanagementsystem.model.entity.enumeration.Departement;
import fr.cyu.schoolmanagementsystem.service.*;
import fr.cyu.schoolmanagementsystem.util.Gmailer;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminWebController {

    private final StudentService studentService;

    private final EnrollmentService enrollmentService;

    private final RequestService requestService;

    private final AdminService adminService;

    private final TeacherService teacherService;

    private final CourseService courseService;
    private final  AssignmentService assignmentService;
    private final  GradeService gradeService;
    private final CourseStatisticsService courseStatisticsService;

    public AdminWebController(StudentService studentService,CourseService courseService, CourseStatisticsService courseStatisticsService, GradeService gradeService, AssignmentService assignmentService, TeacherService teacherService, EnrollmentService enrollmentService, RequestService requestService, AdminService adminService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
        this.requestService = requestService;
        this.adminService = adminService;
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.assignmentService=assignmentService;
        this.courseStatisticsService= courseStatisticsService;
        this.gradeService=gradeService;
    }

    @GetMapping("/{id}")
    public String showDashboard(@PathVariable("id") UUID id, Model model) {
        Admin admin = adminService.getAdmin(id);

        double nbrVerifiedStudents= studentService.getVerifiedStudentCount();
        double nbrRequestsPending= requestService.getTotalPendingRequests();
        int nbrVerifiedTeachers= teacherService.getAllVerifiedTeachers().size();


        // Use CourseStatisticsService to get precomputed statistics
        List<CourseDTO> allCourses = courseService.getAllCourses();
        Map<UUID, Double> courseAverageGrades = allCourses.stream()
                .collect(Collectors.toMap(CourseDTO::getId, course -> courseStatisticsService.getCourseAverage(course.getId())));

        //passing students by department
        Map<Departement, Double> passingStudentsByDepartment = new HashMap<>();
        Map<UUID, Double> passingStudentsByCourse = new HashMap<>();
        Map<Departement, List<CourseDTO>> coursesByDepartment = new HashMap<>();
        Set<UUID> totalPassingStudents = new HashSet<>();

        for (Departement department : Departement.values()) {
            List<CourseDTO> coursesInDepartment = courseService.getCoursesInDepartment(department.getName());
            double totalStudentsInDepartment = 0;
            double passingStudents = 0;

            for (CourseDTO course : coursesInDepartment) {

                List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByCourseId(course.getId());
                if (enrollments.isEmpty()) {
                    continue; // Ignore courses with no enrollments (no grades)
                }

                double passingStudentsInCourse = 0;
                for (EnrollmentDTO enrollment : enrollments) {
                    double averageGrade = gradeService.calculateAverageGradeForEnrollment(enrollment.getId());
                    if (averageGrade >= 10.0) {
                        totalPassingStudents.add(enrollment.getStudentId());
                        passingStudentsInCourse++;
                    }else {
                        totalPassingStudents.remove(enrollment.getStudentId());
                    }
                }
                passingStudents += passingStudentsInCourse;

                totalStudentsInDepartment += enrollments.size();
                double percentageCourse= enrollments.size() > 0 ? (passingStudentsInCourse / enrollments.size()) * 100 : 0;
                passingStudentsByCourse.put(course.getId(), percentageCourse);

            }

            double percentageDepartment = totalStudentsInDepartment > 0 ? (passingStudents / totalStudentsInDepartment) * 100 : 0;

            passingStudentsByDepartment.put(department, percentageDepartment);
            coursesByDepartment.put(department, coursesInDepartment);

        }
        model.addAttribute("totalPassingStudents", nbrVerifiedStudents > 0 ? (totalPassingStudents.size()/nbrVerifiedStudents)*100 : 0);
        model.addAttribute("admin", admin);
        model.addAttribute("coursesByDepartment", coursesByDepartment);
        model.addAttribute("passingStudentsByDepartment", passingStudentsByDepartment);
        model.addAttribute("passingStudentsByCourse", passingStudentsByCourse);
        model.addAttribute("averageGradesByCourse", courseAverageGrades);
        model.addAttribute("nbrRequestsPending", nbrRequestsPending);
        model.addAttribute("nbrVerifiedStudents", nbrVerifiedStudents);
        model.addAttribute("nbrVerifiedTeachers", nbrVerifiedTeachers);
        return "admin/dashboard";
    }


    @PostMapping("/{adminId}/courses/create")
    public String createCourse(
            @PathVariable("adminId") String adminId,
            @RequestParam("department") String department,
            @RequestParam("teacher") String teacherEmail,
            @RequestParam("courseName") String courseName,
            @RequestParam("courseDescription") String courseDescription,
            @RequestParam("dayOfWeek") String dayOfWeek,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            Model model) throws GeneralSecurityException, IOException, MessagingException {

        // Récupérer l'enseignant par son email
        Optional<Teacher> optionalTeacher = teacherService.getTeacherByEmail(teacherEmail);

        // Vérifier si l'enseignant est trouvé
        if (!optionalTeacher.isPresent()) {
            model.addAttribute("flashMessage", "teacherNotFound");
            return "redirect:/admin/" + adminId + "/courses";
        }


        Teacher teacher = optionalTeacher.get();

        // Vérification de la disponibilité du professeur
        Course newCourse = new Course();
        newCourse.setDayOfWeek(DayOfWeek.valueOf(dayOfWeek.toUpperCase()));
        newCourse.setStartTime(LocalTime.parse(startTime));
        newCourse.setEndTime(LocalTime.parse(endTime));

        if (!teacherService.isAvailable(teacher, newCourse)) {

            return "redirect:/admin/" + adminId + "/courses?flashMessage=teacherNotAvailable";
        }

// Vérifier si un cours avec le même nom est programmé à la même journée et aux mêmes horaires pour cet enseignant
        List<CourseDTO> existingCourses = courseService.getCoursesByTeacherId(teacher.getId());
        for (CourseDTO existingCourse : existingCourses) {
            // Vérification du nom et du jour
            if (existingCourse.getName().equals(courseName) && existingCourse.getDayOfWeek() == newCourse.getDayOfWeek() && existingCourse.getStartTime() == newCourse.getStartTime() && existingCourse.getEndTime() == newCourse.getEndTime()) {
                // Vérification de chevauchement des horaires
                boolean hasTimeConflict =
                        existingCourse.getStartTime().isBefore(newCourse.getEndTime());

                if (hasTimeConflict) {
                    model.addAttribute("flashMessage", "duplicateCourseConflict");
                    return "redirect:/admin/" + adminId + "/courses";
                }
            }
        }

        // Créer et ajouter le cours
        CourseDTO course = new CourseDTO();
        course.setName(courseName);
        course.setDescription(courseDescription);
        course.setTeacher(teacher);
        course.setDayOfWeek(DayOfWeek.valueOf(dayOfWeek.toUpperCase()));
        course.setStartTime(LocalTime.parse(startTime));
        course.setEndTime(LocalTime.parse(endTime));



        courseService.addCourse(course);

// Notification par email
        String link = "http://localhost:8080/";
        Gmailer gmailer = new Gmailer();
        gmailer.sendMail(
                "Vous avez été affecté à l'enseignement d'un nouveau cours.",
                "<html>" +
                        "<body>" +
                        "<h2>Vous avez été affecté à l'enseignement d'un nouveau cours.</h2>" +
                        "<p>Voici les détails :</p>" +
                        "<ul>" +
                        "<li><strong>Cours :</strong> " + courseName + "</li>" +
                        "<li><strong>Description :</strong> " + courseDescription + "</li>" +
                        "<li><strong>Jour :</strong> " + dayOfWeek + "</li>" +
                        "<li><strong>Heure :</strong> de " + startTime + " à " + endTime + "</li>" +
                        "</ul>" +
                        "<p>Pour consulter vos cours et plus de détails, connectez-vous à votre espace enseignant :</p>" +
                        "<p><a href='" + link + "' style='color: #1a73e8; text-decoration: none;'>Voir mes cours</a></p>" +
                        "<br>" +
                        "<p>Cordialement,</p>" +
                        "<p><strong>L'équipe de gestion du système</strong></p>" +
                        "</body>" +
                        "</html>",
                teacher.getEmail()
        );


        // Rediriger vers la liste des cours
        return "redirect:/admin/" + adminId + "/courses";
    }



    @PostMapping("/{adminId}/teachers/{department}")
    @ResponseBody
    public List<Teacher> getTeachersByDepartment(@PathVariable("adminId") UUID adminId,
                                                 @PathVariable("department") String department) {
        // Convertir la chaîne du département en un objet de l'énumération Departement
        Departement departementEnum = Departement.fromDisplayName(department);

        // Récupérer les enseignants associés à ce département
        List<Teacher> teachers = teacherService.getTeachersByDepartment(departementEnum.name());

        // Vérifiez si la liste des enseignants est vide et renvoyez une réponse adéquate
        if (teachers == null || teachers.isEmpty()) {
            return Collections.emptyList();  // Retourner un tableau vide si aucun professeur n'est trouvé
        }

        // Retourner la liste des enseignants sous forme de JSON
        return teachers.stream()
                .map(teacher -> new Teacher(
                        teacher.getFirstname(),
                        teacher.getLastname(),
                        teacher.getEmail(),
                        teacher.getPassword(),
                        Departement.fromDisplayName(teacher.getDepartment()),
                        teacher.getSalt()))
                .collect(Collectors.toList());
    }


    @GetMapping ("/{id}/courses")
    public String showCreateCoursePage(@PathVariable ("id") UUID adminId,Model model, RedirectAttributes redirectAttributes) {
        try {
        Admin admin = adminService.getAdmin(adminId);

        // Récupérer tous les en attente
        List<CourseDTO> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        model.addAttribute("user", admin);
        // Récupérer tous les départements à partir de l'énumération
        model.addAttribute("departments", Departement.values());
        model.addAttribute("admin", admin);

        return "admin/courses/create-course";

        }catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("flashError", "Vous n'êtes pas admin.");
            return "redirect:/noaccess";
        }
    }

    @RequestMapping(value = {"/{id}/courses/{courseId}"}, method = RequestMethod.GET)
    public String showCourseDashboard(@PathVariable UUID id, @PathVariable UUID courseId, Model model, RedirectAttributes redirectAttributes, HttpSession session) {

        // Log - Début de la méthode

        System.out.println("Affichage du tableau de bord du cours : " + courseId + " pour l'utilisateur : " + id);

        Optional<CourseDTO> course = courseService.getCourseById(courseId);

        if (course.isPresent()) {

            System.out.println("Cours trouvé : " + course.get().getName()); // Log - Cours trouvé

            List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
            System.out.println("Nombre d'inscriptions pour le cours : " + enrollments.size()); // Log - Nombre d'inscriptions
            boolean isAssignedTeacher = course.get().getTeacher().getId().equals(id);
            boolean isAdmin = adminService.isAdmin(id);
            boolean isEnrolledStudent = enrollments.stream().anyMatch(enrollment -> enrollment.getStudentId().equals(id));

            // Tous les devoirs associés à ce cours
            List<AssignmentDTO> assignments = assignmentService.getAllAssignmentsByCourseId(courseId);
            System.out.println("Nombre de devoirs pour le cours : " + assignments.size()); // Log - Nombre de devoirs

            Map<UUID, Double> averageGrades = new HashMap<>();
            Map<UUID, Double> minGrade = new HashMap<>();
            Map<UUID, Double> maxGrade = new HashMap<>();


            for (AssignmentDTO assignment : assignments) {
                System.out.println("Calcul des moyennes pour le devoir : " + assignment.getId());

                Double grade = gradeService.calculateAverageGradeForAssignment(assignment.getId());
                Double minGradeValue = gradeService.getMinGradeForAssignment(assignment.getId());
                Double maxGradeValue = gradeService.getMaxGradeForAssignment(assignment.getId());

                // Log - Valeurs calculées pour chaque devoir
                System.out.println("Moyenne pour le devoir " + assignment.getId() + ": " + grade);
                System.out.println("Note min pour le devoir " + assignment.getId() + ": " + minGradeValue);
                System.out.println("Note max pour le devoir " + assignment.getId() + ": " + maxGradeValue);

                averageGrades.put(assignment.getId(), grade);
                minGrade.put(assignment.getId(), minGradeValue);
                maxGrade.put(assignment.getId(), maxGradeValue);
            }

            model.addAttribute("assignments", assignments);
            model.addAttribute("averageGrades", averageGrades);
            model.addAttribute("minGrade", minGrade);
            model.addAttribute("maxGrade", maxGrade);
            model.addAttribute("canViewDetails", isAdmin || isAssignedTeacher || isEnrolledStudent);
            model.addAttribute("isAssignedTeacher", isAssignedTeacher);
            model.addAttribute("isEnrolledStudent", isEnrolledStudent);
            model.addAttribute("course", course.get());
            model.addAttribute("admin", adminService.getAdmin(id));


            List<Teacher> teachers =getTeachersByDepartment(id,course.get().getTeacher().getDepartment());
            model.addAttribute("availableTeachers",teachers);

            System.out.println("Retour vers la vue du tableau de bord du cours.");
            return "admin/course_details";

        } else {
            redirectAttributes.addFlashAttribute("error", "Ce cours n'existe pas");
            // Log - Accès non autorisé
            System.out.println("Accès refusé pour l'utilisateur : " + id + " pour le cours " + courseId);
            return "redirect:admin/"+ id +"/courses";
        }

    }




    @GetMapping("/{id}/students")
    public String getAllStudents(Model model, @PathVariable String id) {
        List<StudentDTO> students = studentService.getAllStudents();
        Admin admin = adminService.getAdminByEmail("schoolmanagementjee@gmail.com");
        model.addAttribute("admin", admin);
        model.addAttribute("students", students);

        return "admin/students/list";
    }

    @GetMapping("/{id}/teachers")
    public String getAllTeachers(Model model, @PathVariable String id) {
        List<TeacherDTO> teachers = teacherService.getAllTeachers();
        Admin admin = adminService.getAdminByEmail("schoolmanagementjee@gmail.com");
        model.addAttribute("admin", admin);
        model.addAttribute("teachers", teachers);
        return "admin/teachers/list"; // JSP page name: students/list.jsp
    }

    @GetMapping("/{adminId}/students/{studentId}")
    public String getStudentById(@PathVariable("adminId") UUID adminId, @PathVariable("studentId") UUID studentId, Model model, RedirectAttributes redirectAttributes) {
        Optional<StudentDTO> student = studentService.getStudentById(studentId);

        if (student.isPresent()) {
            Admin admin = adminService.getAdminByEmail("schoolmanagementjee@gmail.com");
            model.addAttribute("admin", admin);
            model.addAttribute("student", student.get());
            System.out.println("je suis la ");
            return "admin/students/view"; // JSP page name: students/view.jsp
        } else {
            redirectAttributes.addFlashAttribute("error", "Student not found");
            return "redirect:/admin/students"; // Redirect to the list page if student not found
        }
    }

    @GetMapping("/{adminId}/teachers/{teacherId}")
    public String getTeacherById(@PathVariable("adminId") UUID adminId, @PathVariable("teacherId") UUID teacherId, Model model, RedirectAttributes redirectAttributes) {
        Optional<TeacherDTO> teacherDTO = teacherService.getTeacherById(teacherId);

        if (teacherDTO.isPresent()){
            Admin admin = adminService.getAdminByEmail("schoolmanagementjee@gmail.com");
            model.addAttribute("admin", admin);
            model.addAttribute("teacher", teacherDTO.get());
            System.out.println("je suis la ");
            return "admin/teachers/view"; // JSP page name: students/view.jsp
        } else {
            redirectAttributes.addFlashAttribute("error", "Student not found");
            return "redirect:/admin/students"; // Redirect to the list page if student not found
        }
    }



    @GetMapping("{adminId}/students/{id}/courses")
    public String showCoursesForStudent(@PathVariable UUID id, Model model, RedirectAttributes redirectAttributes, @PathVariable String adminId) {
        List<CourseDTO> courses = enrollmentService.getCoursesForStudent(id);
        Optional<StudentDTO> student = studentService.getStudentById(id);
        if (courses != null && student.isPresent()) {
            model.addAttribute("courses", courses);
            model.addAttribute("student", student.get());
            Admin admin = adminService.getAdminByEmail("schoolmanagementjee@gmail.com");
            model.addAttribute("admin", admin);
            return "admin/students/courses"; // JSP page name: students/courses.jsp
        } else {
            redirectAttributes.addFlashAttribute("error", "Courses not found for student");
            return "redirect:/admin";
        }
    }
    @GetMapping("{adminId}/teachers/{id}/courses")
    public String showCoursesForTeacher(@PathVariable String adminId, @PathVariable String id,Model model, RedirectAttributes redirectAttributes){
        List<CourseDTO> courses = courseService.getCoursesByTeacherId(UUID.fromString(id));
        Optional<TeacherDTO> teacherDTO = teacherService.getTeacherById(UUID.fromString(id));
        if (courses != null && teacherDTO.isPresent()) {
            model.addAttribute("courses", courses);
            model.addAttribute("teacher", teacherDTO.get());
            Admin admin = adminService.getAdminByEmail("schoolmanagementjee@gmail.com");
            model.addAttribute("admin", admin);
            return "admin/teachers/courses"; // JSP page name: students/courses.jsp
        } else {
            redirectAttributes.addFlashAttribute("error", "Courses not found for student");
            return "redirect:/admin";
        }



    }

    @GetMapping("/new")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        return "admin/students/form"; // JSP page for the form to add a new student
    }

    @PostMapping("/students")
    public String addStudent(@Valid @ModelAttribute("student") StudentDTO studentDTO,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            UUID newStudentId = studentService.addStudent(studentDTO);
            redirectAttributes.addFlashAttribute("message", "Student created successfully");
            return "redirect:/admin/students/" + newStudentId; // Redirect to the new student's view page
        } catch (RuntimeException ex) {
            model.addAttribute("error", "Conflict: " + ex.getMessage());
            return "admin/students/form"; // Reload the form with an error message
        }
    }

    @GetMapping("{adminId}/students/{id}/delete")
    public String deleteStudentById(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes, @PathVariable String adminId) {
        try {
            studentService.deleteStudent(id);

            return "redirect:/admin/"+adminId+"/students?flashMessage=StudentDeletedSuccessfully"; // Redirect to the list of students after deletion
        } catch (RuntimeException ex) {

            return "redirect:/admin/"+adminId+"/students?flashMessage=notDeleted"; // Redirect back to the list with an error message
        }
    }


    @GetMapping("{adminId}/teachers/{id}/delete")
    public String deleteTeacherById(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes, @PathVariable String adminId) {
        try {
            teacherService.deleteTeacherById(id);

            return "redirect:/admin/"+adminId+"/teachers?flashMessage=StudentDeletedSuccessfully"; // Redirect to the list of students after deletion
        } catch (RuntimeException ex) {

            return "redirect:/admin/"+adminId+"/teachers?flashMessage=notDeleted"; // Redirect back to the list with an error message
        }
    }

    @GetMapping("/{id}/requests")
    public String showPendingRequests(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {

        try {
            Admin admin = adminService.getAdmin(UUID.fromString(id));

            // Récupérer les demandes en attente
            List<RegistrationRequest> pendingTeacherRequests = requestService.getPendingTeacherRequests();
            List<RegistrationRequest> pendingStudentRequests = requestService.getPendingStudentRequests();

            model.addAttribute("admin", admin);
            model.addAttribute("pendingTeacherRequests", pendingTeacherRequests);
            model.addAttribute("pendingStudentRequests", pendingStudentRequests);
            return "admin/requests"; // Chemin vers votre JSP

        }catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("flashError", "Vous n'êtes pas admin.");
            return "redirect:/admin/requests";
        }

    }

    @PostMapping("/{adminId}/requests/{requestId}/approve")
    public String approveRequest(@PathVariable("adminId") UUID adminId, @PathVariable("requestId") UUID requestId, RedirectAttributes redirectAttributes) {
        try {
            requestService.approveRegistrationRequest(requestId);
            redirectAttributes.addFlashAttribute("flashSuccess", "Demande approuvée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flashError", "Erreur lors de l'approbation de la demande : " + e.getMessage());
        }
        return "redirect:/admin/" + adminId + "/requests";
    }

    @PostMapping("/reject/{requestId}")
    public String rejectRequest(@PathVariable UUID requestId, RedirectAttributes redirectAttributes) {
        try {
            requestService.rejectRegistrationRequest(requestId);
            redirectAttributes.addFlashAttribute("flashSuccess", "Demande rejetée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("flashError", "Erreur lors du rejet de la demande : " + e.getMessage());
        }
        return "redirect:/admin/requests"; // Redirige vers la liste des demandes
    }
}
