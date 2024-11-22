package fr.cyu.schoolmanagementsystem.controller.web;

import fr.cyu.schoolmanagementsystem.model.dto.*;
import fr.cyu.schoolmanagementsystem.model.entity.Grade;
import fr.cyu.schoolmanagementsystem.model.entity.Student;
import fr.cyu.schoolmanagementsystem.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/students")
public class StudentWebController {

    private final StudentService studentService;

    private final EnrollmentService enrollmentService;
    private final CourseService courseService;
    private final GradeService gradeService;
    private final AssignmentService assignmentService;

    @Autowired
    public StudentWebController(StudentService studentService, EnrollmentService enrollmentService, CourseService courseService, GradeService gradeService, AssignmentService assignmentService) {
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
        this.gradeService = gradeService;
        this.assignmentService = assignmentService;
    }


    @RequestMapping(value = {"/{id}/courses/{courseId}"}, method = RequestMethod.GET)
    public String showCourseDashboard(@PathVariable UUID id, @PathVariable("courseId") UUID courseId, Model model, RedirectAttributes redirectAttributes, HttpSession session) {
        // Log - Début de la méthode
        System.out.println("Affichage du tableau de bord du cours : " + courseId + " pour l'utilisateur : " + id);
        Optional<CourseDTO> course = courseService.getCourseById(courseId);

        if (course.isPresent()) {

            System.out.println("Cours trouvé : " + course.get().getName()); // Log - Cours trouvé

            List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
            System.out.println("Nombre d'inscriptions pour le cours : " + enrollments.size()); // Log - Nombre d'inscriptions

            boolean isAssignedTeacher = false;
            boolean isAdmin = false;
            boolean isEnrolledStudent = enrollments.stream().anyMatch(enrollment -> enrollment.getStudentId().equals(id));

            // Log - Statut d'autorisation
            System.out.println("isAssignedTeacher: " + isAssignedTeacher);
            System.out.println("isEnrolledStudent: " + isEnrolledStudent);

            if (isEnrolledStudent) {


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

                System.out.println("Retour vers la vue du tableau de bord du cours.");

            }
            model.addAttribute("student", studentService.getStudentById(id).get());
            return "students/course_details";


        } else {
            redirectAttributes.addFlashAttribute("error", "Cours introuvable.");
            // Log - Cours introuvable
            System.out.println("Cours introuvable pour le cours ID : " + courseId);
            return "redirect:student/"+id+"/courses"; // Redirection vers la liste des étudiants si le cours est introuvable
        }
    }



    @PostMapping("/{studentId}/courses/{courseId}/enroll")
    public String enrollStudentInCourse(@PathVariable("studentId") UUID studentId,
                                        @PathVariable("courseId") UUID courseId,
                                        RedirectAttributes redirectAttributes) {
        // Créer un DTO d'inscription avec les IDs
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO();
        enrollmentDTO.setStudentId(studentId);
        enrollmentDTO.setCourseId(courseId);

        // Appeler le service pour inscrire l'étudiant au cours
        boolean success = enrollmentService.enrollStudentToCourse(enrollmentDTO);

        if (success) {
            // Si l'inscription réussie, rediriger vers la liste des cours avec un message de succès
            redirectAttributes.addFlashAttribute("successMessage", "Inscription réussie au cours !");
            return "redirect:/students/" + studentId + "?flashMessage=enrollmentSuccess";
        } else {
            // Si l'inscription échoue, rediriger avec un message d'erreur
            redirectAttributes.addFlashAttribute("errorMessage", "Inscription échouée. Le cours ou l'étudiant est introuvable.");
            return "redirect:/students/" + studentId + "/?flashMessage=enrollmentFailed";
        }
    }



    @GetMapping("/{id}")
    public String showStudentDashboard(@PathVariable("id") UUID studentId, Model model, RedirectAttributes redirectAttributes) {


        Optional<StudentDTO> student = studentService.getStudentById(studentId);
        double studentGlobalAverage= gradeService.calculateAverageGradeForStudent(studentId);
        if (student.isPresent()) {



            List<CourseDTO> courses = enrollmentService.getCoursesForStudent(studentId);

            boolean noCourses = courses.isEmpty();  // Si la liste des cours est vide, noCourses sera true.
            model.addAttribute("noCourses", noCourses);

            System.out.println(noCourses);

            Map<UUID, String> courseAverages = new HashMap<>();
            Map<UUID, Double> courseMinAverages = new HashMap<>();
            Map<UUID, Double> courseMaxAverages = new HashMap<>();
            Map<UUID, Double> studentAverages = new HashMap<>();




            for (CourseDTO course : courses) {
                Optional<EnrollmentDTO> enrollment= enrollmentService.getEnrollmentByStudentIdAndCourseId(studentId, course.getId());
                double averageGrade = gradeService.calculateAverageGradeForCourse(course.getId());
                double minAverageGrade = gradeService.getMinAverageForCourse(course.getId());
                double maxAverageGrade = gradeService.getMaxAverageForCourse(course.getId());
                double studentAverage = gradeService.calculateAverageGradeForEnrollment(enrollment.get().getId());


                // Vérifier si l'étudiant n'a pas de note pour ce cours
                if (Double.isNaN(studentAverage)) {
                    courseAverages.put(course.getId(), "Pas de note pour ce cours");
                } else {
                    // Si l'étudiant a une note, on met la moyenne dans le map
                    courseAverages.put(course.getId(), String.format("%.2f", studentAverage));
                }





                courseMinAverages.put(course.getId(), minAverageGrade);
                courseMaxAverages.put(course.getId(), maxAverageGrade);
                studentAverages.put(course.getId(), studentAverage);

                System.out.println(courseAverages.get(course.getId()));
            }

            System.out.println(student.get().getId());

            model.addAttribute("student", student.get());
            model.addAttribute("courses", courses);
            model.addAttribute("courseAverages", courseAverages);
            model.addAttribute("minAverages", courseMinAverages);
            model.addAttribute("maxAverages", courseMaxAverages);
            model.addAttribute("studentGlobalAverage", studentGlobalAverage);
            model.addAttribute("studentAverages", studentAverages);
            return "students/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Student not found");
            return "redirect:/students"; // Redirect to the list page if student not found
        }
    }

    @GetMapping("/{studentId}/enroll")
    public String viewCourses(@PathVariable("studentId") UUID studentId, Model model) {

        // Récupérer l'étudiant par son ID
        Optional<StudentDTO> optionalStudent = studentService.getStudentById(studentId);

        // Vérifier si l'étudiant existe
        if (!optionalStudent.isPresent()) {
            // Gérer le cas où l'étudiant n'est pas trouvé
            return "error";  // Ou une autre vue de votre choix
        }

        // Récupérer la liste des cours auxquels l'étudiant est déjà inscrit
        List<EnrollmentDTO> studentEnrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
        Set<UUID> enrolledCourseIds = studentEnrollments.stream()
                .map(EnrollmentDTO::getCourseId)  // Supposons que EnrollmentDTO a un `getCourseId()` retournant l'ID du cours
                .collect(Collectors.toSet());

        // Récupérer la liste des cours disponibles
        List<CourseDTO> availableCourses = courseService.getAllCourses();

        // Filtrer les cours pour exclure ceux auxquels l'étudiant est déjà inscrit
        List<CourseDTO> availableCoursesToEnroll = availableCourses.stream()
                .filter(course -> !enrolledCourseIds.contains(course.getId()))  // Exclure les cours déjà suivis
                .collect(Collectors.toList());

        System.out.println("Available courses: " + availableCoursesToEnroll.size());
        for (CourseDTO course : availableCoursesToEnroll) {
            System.out.println(course.getName());
        }

        // Ajouter les attributs au modèle
        model.addAttribute("noCourses", availableCoursesToEnroll.isEmpty());
        model.addAttribute("student", optionalStudent.get());
        model.addAttribute("availableCourses", availableCoursesToEnroll);

        return "students/student_courses";  // La vue qui affiche la liste des cours disponibles
    }




}
