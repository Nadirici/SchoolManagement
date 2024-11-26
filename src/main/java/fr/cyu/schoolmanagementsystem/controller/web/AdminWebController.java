package fr.cyu.schoolmanagementsystem.controller.web;

import fr.cyu.schoolmanagementsystem.model.dto.*;
import fr.cyu.schoolmanagementsystem.model.entity.Admin;
import fr.cyu.schoolmanagementsystem.model.entity.RegistrationRequest;
import fr.cyu.schoolmanagementsystem.model.entity.Teacher;
import fr.cyu.schoolmanagementsystem.model.entity.enumeration.Departement;
import fr.cyu.schoolmanagementsystem.service.*;
import fr.cyu.schoolmanagementsystem.util.Gmailer;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
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

    public AdminWebController(StudentService studentService,CourseService courseService, GradeService gradeService, AssignmentService assignmentService, TeacherService teacherService, EnrollmentService enrollmentService, RequestService requestService, AdminService adminService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
        this.requestService = requestService;
        this.adminService = adminService;
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.assignmentService=assignmentService;
        this.gradeService=gradeService;
    }

    @GetMapping("/{id}")
    public String showDashboard(@PathVariable("id") UUID id, Model model) {
        Admin admin = adminService.getAdmin(id);
        model.addAttribute("admin", admin);
        return "admin/dashboard";
    }

    @PostMapping("/{adminId}/courses/create")
    public String createCourse(
            @PathVariable("adminId") String adminId,
            @RequestParam("department") String department,
            @RequestParam("teacher") String teacherEmail,
            @RequestParam("courseName") String courseName,
            @RequestParam("courseDescription") String courseDescription,
            Model model) {

        // Récupérer l'enseignant par son email
        Optional<Teacher> optionalTeacher = teacherService.getTeacherByEmail(teacherEmail);

        // Vérifier si l'enseignant est présent dans l'Optional
        Teacher teacher;
        if (optionalTeacher.isPresent()) {
            teacher = optionalTeacher.get();
        } else {
            System.out.println("Teacher not found!");

            return "/admin/"+adminId+"/courses?flashMessage=teacherNotfound"; // Retourner à la page de création si l'enseignant n'est pas trouvé
        }

        // Créer un objet CourseDTO avec les informations du formulaire
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName(courseName);
        courseDTO.setDescription(courseDescription);
        courseDTO.setTeacher(teacher); // Mapper l'enseignant à un TeacherDTO

        try {
            // Utiliser CourseService pour ajouter le cours
            UUID courseId = courseService.addCourse(courseDTO);  // Sauvegarder le cours dans la base de données

            // Ajouter les données au modèle
            model.addAttribute("department", department);
            model.addAttribute("teacher", teacher);
            model.addAttribute("courseName", courseName);
            model.addAttribute("courseDescription", courseDescription);
            model.addAttribute("successMessage", "Course created successfully!");


            String link = "http://localhost:8080/";
            Gmailer gmailer = new Gmailer();
            gmailer.sendMail(
                    "Nouvelle affectation à un cours",
                    "Bonjour " + teacher.getFirstname() + " " + teacher.getLastname() + ",<br><br>" +
                            "Vous avez été affecté ç l'enseignement d'un nouveau cours.<br><br>" +
                            "Voici les détails :<br>" +
                            "- Cours : " + courseName + "<br>" +
                            "- Description : " + courseDescription + "<br><br>" +
                            "Pour consulter vos cours et plus de détails, connectez-vous à votre espace enseignant :<br>" +
                            "<a href='" + link + "'>Voir mes cours</a><br><br>" +
                            "Cordialement,<br>" +
                            "L'équipe de gestion du système.",
                    teacherEmail
            );

            // Redirection vers la page des cours de l'administrateur avec un message flash
            return "redirect:/admin/" + adminId + "/courses?flashMessage=courseCreated";
        } catch (RuntimeException e) {
            // Gestion des erreurs si l'ajout du cours échoue
            model.addAttribute("errorMessage", e.getMessage());
            return "/admin/"+adminId+"/courses";  // Retourner à la page de création en cas d'erreur
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
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
    public String getAllStudents(Model model) {
        List<StudentDTO> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "admin/students/list"; // JSP page name: students/list.jsp
    }

    @GetMapping("/{adminId}/students/{studentId}")
    public String getStudentById(@PathVariable("adminId") UUID adminId, @PathVariable("studentId") UUID studentId, Model model, RedirectAttributes redirectAttributes) {
        Optional<StudentDTO> student = studentService.getStudentById(studentId);

        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            return "admin/students/view"; // JSP page name: students/view.jsp
        } else {
            redirectAttributes.addFlashAttribute("error", "Student not found");
            return "redirect:/admin/students"; // Redirect to the list page if student not found
        }
    }

    @GetMapping("/students/{id}/courses")
    public String showCoursesForStudent(@PathVariable UUID id, Model model, RedirectAttributes redirectAttributes) {
        List<CourseDTO> courses = enrollmentService.getCoursesForStudent(id);
        Optional<StudentDTO> student = studentService.getStudentById(id);
        if (courses != null && student.isPresent()) {
            model.addAttribute("courses", courses);
            model.addAttribute("student", student.get());
            return "admin/students/courses"; // JSP page name: students/courses.jsp
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

    @DeleteMapping("/students/{id}")
    public String deleteStudentById(@PathVariable("id") UUID id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("message", "Student deleted successfully");
            return "redirect:/admin/students"; // Redirect to the list of students after deletion
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("error", "Conflict: " + ex.getMessage());
            return "redirect:/admin/students"; // Redirect back to the list with an error message
        }
    }

    @GetMapping("/{id}/requests")
    public String showPendingRequests(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {

        try {
            Admin admin = adminService.getAdmin(UUID.fromString(id));

            // Récupérer les demandes en attente
            List<RegistrationRequest> pendingTeacherRequests = requestService.getPendingTeacherRequests();
            List<RegistrationRequest> pendingStudentRequests = requestService.getPendingStudentRequests();

            model.addAttribute("adminId", admin.getId());
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
