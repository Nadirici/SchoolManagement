package fr.cyu.schoolmanagementsystem.controller.web;

import fr.cyu.schoolmanagementsystem.model.dto.*;
import fr.cyu.schoolmanagementsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import java.util.*;

@Controller

public class CourseWebController {

    private final TeacherService teacherService;
    private final EnrollmentService enrollmentService;
    private final CourseService courseService;
    private final GradeService gradeService;
    private final AssignmentService assignmentService;
    private final StudentService studentService;
    private final AdminService adminService;

    @Autowired
    public CourseWebController(TeacherService teacherService, AdminService adminService, EnrollmentService enrollmentService, CourseService courseService, GradeService gradeService, AssignmentService assignmentService, StudentService studentService) {
        this.teacherService = teacherService;
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
        this.gradeService = gradeService;
        this.assignmentService = assignmentService;
        this.adminService = adminService;
        this.studentService = studentService;
    }

    @RequestMapping(value = {"/*/{id}/courses/{courseId}"}, method = RequestMethod.GET)
    public String showCourseDashboard(@PathVariable("id") UUID id, @PathVariable("courseId") UUID courseId, Model model, RedirectAttributes redirectAttributes) {

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

            // Log - Statut d'autorisation
            System.out.println("isAssignedTeacher: " + isAssignedTeacher);
            System.out.println("isAdmin: " + isAdmin);
            System.out.println("isEnrolledStudent: " + isEnrolledStudent);

            if (isAdmin || isAssignedTeacher || isEnrolledStudent) {


                // Collect all assignments related to the course
                List<AssignmentDTO> assignments = assignmentService.getAllAssignmentsByCourseId(courseId);
                System.out.println("Nombre de devoirs pour le cours : " + assignments.size()); // Log - Nombre de devoirs

                Map<UUID, Double> averageGrades = new HashMap<>();
                Map<UUID, Double> minGrade = new HashMap<>();
                Map<UUID, Double> maxGrade = new HashMap<>();
                Map<UUID, Double> studentAssignmentGrades = new HashMap<>();


                for (AssignmentDTO assignment : assignments) {
                    // Log - Calcul des moyennes pour chaque devoir
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


                if (isEnrolledStudent) {

                    Optional<EnrollmentDTO> enrollment = enrollmentService.getEnrollmentByStudentIdAndCourseId(id, courseId);

                    if (enrollment.isPresent()) {
                        // Log - L'étudiant est inscrit
                        System.out.println("L'étudiant " + id + " est inscrit à ce cours.");

                        for (AssignmentDTO assignment : assignments) {
                            Optional<GradeDTO> studentGrade = gradeService.getAllGradesByAssignmentIdAndEnrollmentId(assignment.getId(), enrollment.get().getId());

                            if (studentGrade.isPresent()) {
                                // Log - Note de l'étudiant pour chaque devoir
                                System.out.println("Note de l'étudiant pour le devoir " + assignment.getId() + ": " + studentGrade.get().getScore());
                                studentAssignmentGrades.put(assignment.getId(), studentGrade.get().getScore());
                            } else {
                                System.out.println("Aucune note trouvée pour l'étudiant " + id + " pour le devoir " + assignment.getId());
                            }
                        }
                    } else {
                        System.out.println("L'étudiant " + id + " n'est pas inscrit à ce cours.");
                    }
                }




                model.addAttribute("student", studentService.getStudentById(id).get());
                model.addAttribute("studentAssignmentGrades", studentAssignmentGrades);
                model.addAttribute("assignments", assignments);
                model.addAttribute("averageGrades", averageGrades);
                model.addAttribute("minGrade", minGrade);
                model.addAttribute("maxGrade", maxGrade);
                model.addAttribute("canViewDetails", isAdmin || isAssignedTeacher || isEnrolledStudent);
                model.addAttribute("isAssignedTeacher", isAssignedTeacher);
                model.addAttribute("isEnrolledStudent", isEnrolledStudent);
                model.addAttribute("course", course.get());



                // Log - Avant de retourner la vue
                System.out.println("Retour vers la vue du tableau de bord du cours.");
                return "students/single-course";
            } else {
                redirectAttributes.addFlashAttribute("error", "Vous n'êtes pas autorisé à voir ce cours.");
                // Log - Accès non autorisé
                System.out.println("Accès refusé pour l'utilisateur : " + id + " pour le cours " + courseId);
                return "redirect:/students"; // Redirection vers la liste des étudiants si l'accès est refusé
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Cours introuvable.");
            // Log - Cours introuvable
            System.out.println("Cours introuvable pour le cours ID : " + courseId);
            return "redirect:/students"; // Redirection vers la liste des étudiants si le cours est introuvable
        }
    }






    @RequestMapping(value = {"/*/{id}/courses/{courseId}/grades"}, method = RequestMethod.GET)
    public String showCourseGrades(@PathVariable("id") UUID id, @PathVariable("courseId") UUID courseId, Model model, RedirectAttributes redirectAttributes) {

        Optional<CourseDTO> course = courseService.getCourseById(courseId);
        if (course.isPresent()) {
            List<StudentDTO> students = enrollmentService.getStudentsForCourse(courseId);
            List<AssignmentDTO> assignments = assignmentService.getAllAssignmentsByCourseId(courseId);
            boolean isAssignedTeacher = course.get().getTeacher().getId().equals(id);
            boolean isAdmin = adminService.isAdmin(id);

            if (isAdmin || isAssignedTeacher) {
                List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
                Map<UUID, Double> averageGrades = new HashMap<>();
                Map<UUID, Map<UUID, Double>> assignmentsGrades = new HashMap<>();

                for (EnrollmentDTO enrollment : enrollments) {
                    Map<UUID, Double> studentCourseGrades = new HashMap<>();
                    double average = gradeService.calculateAverageGradeForEnrollment(enrollment.getId());
                    List<GradeDTO> grades = gradeService.getAllGradesByEnrollmentId(enrollment.getId());

                    for (GradeDTO grade : grades) {
                        studentCourseGrades.put(grade.getAssignmentId(), grade.getScore());
                    }
                    assignmentsGrades.put(enrollment.getStudentId(), studentCourseGrades);
                    averageGrades.put(enrollment.getStudentId(), average);
                }

                model.addAttribute("assignmentsGrades", assignmentsGrades);
                model.addAttribute("averageGrades", averageGrades);
            } else {
                redirectAttributes.addFlashAttribute("error", "Vous n'êtes pas autorisé à voir les notes.");
                return "redirect:/students/single-course"; // Redirection vers la page du cours si l'accès est refusé
            }

            model.addAttribute("assignments", assignments);
            model.addAttribute("students", students);
            model.addAttribute("canViewGrades", isAdmin || isAssignedTeacher);


            model.addAttribute("course", course.get());
            return "lists/course_grades";
        } else {
            redirectAttributes.addFlashAttribute("error", "Cours introuvable.");
            return "redirect:/students/single-course"; // Redirection vers la page du cours si le cours est introuvable
        }
    }






}
