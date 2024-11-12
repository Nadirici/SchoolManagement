package fr.cyu.schoolmanagementsystem.controller.web;

import fr.cyu.schoolmanagementsystem.model.dto.CourseDTO;
import fr.cyu.schoolmanagementsystem.model.dto.StudentDTO;
import fr.cyu.schoolmanagementsystem.model.entity.Admin;
import fr.cyu.schoolmanagementsystem.model.entity.RegistrationRequest;
import fr.cyu.schoolmanagementsystem.service.AdminService;
import fr.cyu.schoolmanagementsystem.service.EnrollmentService;
import fr.cyu.schoolmanagementsystem.service.RequestService;
import fr.cyu.schoolmanagementsystem.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminWebController {

    private final StudentService studentService;

    private final EnrollmentService enrollmentService;

    private final RequestService requestService;

    private final AdminService adminService;

    public AdminWebController(StudentService studentService, EnrollmentService enrollmentService, RequestService requestService, AdminService adminService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
        this.requestService = requestService;
        this.adminService = adminService;
    }

    @GetMapping("/{id}")
    public String showDashboard(@PathVariable("id") UUID id, Model model) {
        Admin admin = adminService.getAdmin(id);
        model.addAttribute("admin", admin);
        return "admin/dashboard";
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
