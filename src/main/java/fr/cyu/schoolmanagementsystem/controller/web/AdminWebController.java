package fr.cyu.schoolmanagementsystem.controller.web;

import fr.cyu.schoolmanagementsystem.model.dto.CourseDTO;
import fr.cyu.schoolmanagementsystem.model.dto.StudentDTO;
import fr.cyu.schoolmanagementsystem.model.dto.TeacherDTO;
import fr.cyu.schoolmanagementsystem.model.entity.Admin;
import fr.cyu.schoolmanagementsystem.model.entity.RegistrationRequest;
import fr.cyu.schoolmanagementsystem.model.entity.Teacher;
import fr.cyu.schoolmanagementsystem.model.entity.enumeration.Departement;
import fr.cyu.schoolmanagementsystem.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    public AdminWebController(StudentService studentService,CourseService courseService, TeacherService teacherService, EnrollmentService enrollmentService, RequestService requestService, AdminService adminService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
        this.requestService = requestService;
        this.adminService = adminService;
        this.teacherService = teacherService;
        this.courseService = courseService;
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

            // Redirection vers la page des cours de l'administrateur avec un message flash
            return "redirect:/admin/" + adminId + "/courses?flashMessage=courseCreated";
        } catch (RuntimeException e) {
            // Gestion des erreurs si l'ajout du cours échoue
            model.addAttribute("errorMessage", e.getMessage());
            return "/admin/"+adminId+"/courses";  // Retourner à la page de création en cas d'erreur
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
    public String showCreateCoursePage(@PathVariable ("id") UUID adminId,Model model) {

        Admin admin = adminService.getAdmin(adminId);
        // Récupérer tous les départements à partir de l'énumération
        model.addAttribute("departments", Departement.values());
        model.addAttribute("admin", admin);

        return "admin/courses/create-course"; // Le nom de votre JSP ou template
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
