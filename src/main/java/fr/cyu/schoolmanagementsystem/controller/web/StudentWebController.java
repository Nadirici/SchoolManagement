package fr.cyu.schoolmanagementsystem.controller.web;

import fr.cyu.schoolmanagementsystem.model.dto.*;
import fr.cyu.schoolmanagementsystem.model.entity.Grade;
import fr.cyu.schoolmanagementsystem.model.entity.Student;
import fr.cyu.schoolmanagementsystem.service.*;
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
