package fr.cyu.schoolmanagementsystem.controller.web;

import fr.cyu.schoolmanagementsystem.model.dto.*;
import fr.cyu.schoolmanagementsystem.service.*;
import fr.cyu.schoolmanagementsystem.util.Gmailer;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Controller
@RequestMapping("/teachers")
public class TeacherWebController {

    private final TeacherService teacherService;
    private final EnrollmentService enrollmentService;
    private final CourseService courseService;
    private final GradeService gradeService;
    private final AssignmentService assignmentService;
    private final StudentService studentService;

    @Autowired
    public TeacherWebController(TeacherService teacherService, EnrollmentService enrollmentService, CourseService courseService, GradeService gradeService, AssignmentService assignmentService, StudentService studentService) {
        this.teacherService = teacherService;
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
        this.gradeService = gradeService;
        this.assignmentService = assignmentService;
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public String showTeacherDashboard(@PathVariable("id") UUID teacherId, Model model, RedirectAttributes redirectAttributes) {
        Optional<TeacherDTO> teacher = teacherService.getTeacherById(teacherId);

        if (teacher.isPresent()) {
            List<CourseDTO> courses = courseService.getCoursesByTeacherId(teacherId);
            Map<UUID, Double> courseAverages = new HashMap<>();
            Map<UUID, Double> courseMinAverages = new HashMap<>();
            Map<UUID, Double> courseMaxAverages = new HashMap<>();
            for (CourseDTO course : courses) {
                double averageGrade = gradeService.calculateAverageGradeForCourse(course.getId());
                double minAverageGrade = gradeService.getMinAverageForCourse(course.getId());
                double maxAverageGrade = gradeService.getMaxAverageForCourse(course.getId());
                courseAverages.put(course.getId(), averageGrade);
                courseMinAverages.put(course.getId(), minAverageGrade);
                courseMaxAverages.put(course.getId(), maxAverageGrade);
            }
            model.addAttribute("teacher", teacher.get());
            model.addAttribute("courses", courses);
            model.addAttribute("courseAverages", courseAverages);
            model.addAttribute("minAverages", courseMinAverages);
            model.addAttribute("maxAverages", courseMaxAverages);
            return "teachers/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "teacher not found");
            return "redirect:/teachers"; // Redirect to the list page if student not found
        }
    }

    @GetMapping("/{id}/courses")
    public String showCourses(@PathVariable UUID id, Model model, RedirectAttributes redirectAttributes) {

        Optional<TeacherDTO> teacher = teacherService.getTeacherById(id);

        if (teacher.isPresent()) {
            List<CourseDTO> courses = courseService.getCoursesByTeacherId(id);
            model.addAttribute("courses", courses);
            model.addAttribute("user", teacher.get());

            model.addAttribute("teacher", teacher.get());
            return "teachers/my_courses";
        }else {
            // Si l'inscription échoue, rediriger avec un message d'erreur
            redirectAttributes.addFlashAttribute("errorMessage", "Le professeur est introuvable");
            return "redirect:/teachers/" + id + "/?flashMessage=error";
        }

    }


    @RequestMapping(value = {"/{id}/courses/{courseId}"}, method = RequestMethod.GET)
    public String showCourseDashboard(@PathVariable("id") UUID id, @PathVariable("courseId") UUID courseId, Model model, RedirectAttributes redirectAttributes, HttpSession session) {

        // Log - Début de la méthode

        System.out.println("Affichage du tableau de bord du cours : " + courseId + " pour l'utilisateur : " + id);
        Optional<CourseDTO> course = courseService.getCourseById(courseId);

        if (course.isPresent()) {

            System.out.println("Cours trouvé : " + course.get().getName()); // Log - Cours trouvé

            List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
            System.out.println("Nombre d'inscriptions pour le cours : " + enrollments.size()); // Log - Nombre d'inscriptions

            boolean isAssignedTeacher = course.get().getTeacher().getId().equals(id);
            boolean isAdmin =false;
            boolean isEnrolledStudent = false;

            // Log - Statut d'autorisation
            System.out.println("isAssignedTeacher: " + isAssignedTeacher);
            System.out.println("isAdmin: " + isAdmin);
            System.out.println("isEnrolledStudent: " + isEnrolledStudent);

            if (isAssignedTeacher) {
                // Collect all assignments related to the course
                List<AssignmentDTO> assignments = assignmentService.getAllAssignmentsByCourseId(courseId);
                System.out.println("Nombre de devoirs pour le cours : " + assignments.size()); // Log - Nombre de devoirs

                Map<UUID, Double> averageGrades = new HashMap<>();
                Map<UUID, Double> minGrade = new HashMap<>();
                Map<UUID, Double> maxGrade = new HashMap<>();


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

                model.addAttribute("assignments", assignments);
                model.addAttribute("averageGrades", averageGrades);
                model.addAttribute("minGrade", minGrade);
                model.addAttribute("maxGrade", maxGrade);
                model.addAttribute("canViewDetails", isAdmin || isAssignedTeacher || isEnrolledStudent);
                model.addAttribute("isAssignedTeacher", isAssignedTeacher);
                model.addAttribute("isEnrolledStudent", isEnrolledStudent);
                model.addAttribute("course", course.get());


                System.out.println("Retour vers la vue du tableau de bord du cours.");

            } else {
                redirectAttributes.addFlashAttribute("error", "Vous n'êtes pas autorisé à voir ce cours.");
                System.out.println("Accès refusé pour l'utilisateur : " + id + " pour le cours " + courseId);

            }
            model.addAttribute("teacher", teacherService.getTeacherById(id).get());
            return "teachers/course_details";

        } else {
            redirectAttributes.addFlashAttribute("error", "Cours introuvable.");
            // Log - Cours introuvable
            System.out.println("Cours introuvable pour le cours ID : " + courseId);
            return "redirect:teachers/"+id+"/courses"; // Redirection vers la liste des étudiants si le cours est introuvable
        }
    }


    @GetMapping("/{id}/courses/{courseId}/add-assignment")
    public String showAddAssignmentForm(@PathVariable UUID id,
                                        @PathVariable UUID courseId,
                                        RedirectAttributes redirectAttributes,
                                        Model model) {
        Optional<CourseDTO> courseDTO = courseService.getCourseById(courseId);
        boolean isAssignedTeacher= courseDTO.get().getTeacher().getId().equals(id);
        if (courseDTO.isPresent() && isAssignedTeacher){
            List<StudentDTO> students = enrollmentService.getStudentsForCourse(courseId);
            model.addAttribute("students", students);
            System.out.println("Cours trouvé : " + courseDTO.get().getName());
            model.addAttribute("assignment", new AssignmentDTO());
            model.addAttribute("courseId", courseId);
            model.addAttribute("teacher", teacherService.getTeacherById(id).get());
            return "teachers/add_assignment";
        }else{
            redirectAttributes.addFlashAttribute("error", isAssignedTeacher ? "Ce cours n'existe pas." : "Vous n'enseignez pas ce cours.");
            return "redirect:/teachers/"+id;
        }
        // JSP page for the form to add a new student
    }

    @PostMapping("/{id}/courses/{courseId}/assignment")
    public String createAssignment(@PathVariable UUID id,
                                   @PathVariable UUID courseId,
                                   @RequestParam Map<String, String> params,
                                   RedirectAttributes redirectAttributes,
                                   Model model){
        Optional<CourseDTO> courseDTO = courseService.getCourseById(courseId);
        boolean isAssignedTeacher= courseDTO.get().getTeacher().getId().equals(id);
        if (courseDTO.isPresent() && isAssignedTeacher){
            try{
                AssignmentDTO assignmentDTO = new AssignmentDTO();
                assignmentDTO.setTitle(params.get("title"));
                assignmentDTO.setDescription(params.get("description"));
                assignmentDTO.setCoefficient(Double.parseDouble(params.get("coefficient")));
                assignmentDTO.setCourse(courseDTO.get());

                // Ajouter l'Assignment
                UUID assignmentId = assignmentService.addAssignment(assignmentDTO, courseId);
                // S'assurer que l'assignement est bien persisté

                // Parcourir les paramètres pour extraire les notes des étudiants
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (entry.getKey().startsWith("grade_")) {
                        String studentIdStr = entry.getKey().substring(6); // Supprimer le préfixe "grade_"
                        UUID studentId = UUID.fromString(studentIdStr);
                        Double gradeValue = Double.parseDouble(entry.getValue());

                        Optional<StudentDTO> studentDTO = studentService.getStudentById(studentId);
                        // Récupérer l'enrollment correspondant
                        Optional<EnrollmentDTO> enrollmentOptional = enrollmentService.getEnrollmentByStudentIdAndCourseId(studentId, courseId);
                        if (enrollmentOptional.isPresent()) {
                            GradeDTO gradeDTO = new GradeDTO();
                            gradeDTO.setAssignmentId(assignmentId);
                            gradeDTO.setScore(gradeValue);
                            gradeDTO.setEnrollmentId(enrollmentOptional.get().getId());

                            // Sauvegarder la note
                            gradeService.addGrade(gradeDTO);

                            String link = "http://localhost:8080/";
                            Gmailer gmailer = new Gmailer();
                            gmailer.sendMail(
                                    "Nouvelle note ajoutée à votre compte",
                                    "Bonjour " + studentDTO.get().getFirstname() + " " + studentDTO.get().getLastname() + ",<br><br>" +
                                            "Une nouvelle note a été ajoutée à votre compte sur la plateforme.<br><br>" +
                                            "Voici les détails :<br>" +
                                            "- Cours : " + courseDTO.get().getName() + "<br>" +
                                            "- Devoir : " + assignmentDTO.getTitle() + "<br>" +
                                            "- Note : " + gradeDTO.getScore() + "<br><br>" +
                                            "Pour consulter vos notes et plus de détails, connectez-vous à votre espace étudiant :<br>" +
                                            "<a href='" + link + "'>Voir mes notes</a><br><br>" +
                                            "Cordialement,<br>" +
                                            "L'équipe de gestion du système.",
                                    studentDTO.get().getEmail()
                            );
                        } else {
                            throw new RuntimeException("Inscription non trouvée pour l'étudiant avec ID: " + studentId);
                        }
                    }
                }

                redirectAttributes.addFlashAttribute("success", "Le devoir et les notes ont été ajoutés avec succès.");
                return "redirect:/teachers/"+id+"/courses/"+courseId;
            } catch (RuntimeException ex) {
                System.out.println("erreur de saisie de devoir: "+ ex.getMessage());
                return "teachers/add_assignment"; //Reload the form with an error message
            } catch (IOException | MessagingException | GeneralSecurityException e) {
                throw new RuntimeException(e);
            }

        }else{
            System.out.println("erreur cours non existant ou prof non existant");
            redirectAttributes.addFlashAttribute("error", isAssignedTeacher ? "Ce cours n'existe pas." : "Vous n'enseignez pas ce cours.");
            return "redirect:/teachers/"+id+"/courses/"+courseId;
        }
    }

    @DeleteMapping("/{id}/courses/{courseId}/assignment/{assignmentId}")
    public String deleteAssignment(@PathVariable UUID id,
                                   @PathVariable UUID courseId,
                                   @PathVariable UUID assignmentId,
                                   RedirectAttributes redirectAttributes) {
        try {
            gradeService.deleteGradesByAssignmentId(assignmentId);
            assignmentService.deleteAssignment(assignmentId);
            System.out.println("devoir supprimé");
            redirectAttributes.addFlashAttribute("message", "Assignment deleted successfully");
            return "redirect:/teachers/"+id+"/courses/"+courseId; // Redirect to the list of students after deletion
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            redirectAttributes.addFlashAttribute("error", "Conflict: " + ex.getMessage());
            return "redirect:/teachers/"+id+"/courses/"+courseId; // Redirect back to the list with an error message
        }
    }
    @GetMapping("/{id}/courses/{courseId}/assignment/{assignmentId}")
    public String showUpdateAssignmentAndGradesForm(@PathVariable UUID id,
                                                    @PathVariable UUID courseId,
                                                    @PathVariable UUID assignmentId,
                                                    Model model) throws MessagingException, GeneralSecurityException, IOException {
        Optional<CourseDTO> courseDTO = courseService.getCourseById(courseId);
        Optional<AssignmentDTO> assignmentDTO = assignmentService.getAssignmentById(assignmentId);


        if (courseDTO.isPresent() && assignmentDTO.isPresent() && courseDTO.get().getTeacher().getId().equals(id)) {
            AssignmentDTO assignment = assignmentDTO.get();
            model.addAttribute("assignment", assignment);

            // Fetch grades for students enrolled in the course
            List<StudentDTO> students = enrollmentService.getStudentsForCourse(courseId);
            List<GradeDTO> gradeList = gradeService.getGradesByAssignmentId(assignmentId);
            Map<UUID, GradeDTO> studentGrades = new HashMap<>();
            for (GradeDTO grade : gradeList) {
                Optional<EnrollmentDTO> enrollmentOptional = enrollmentService.getEnrollmentById(grade.getEnrollmentId());
                if (enrollmentOptional.isPresent()) {
                    UUID studentId = enrollmentOptional.get().getStudentId();
                    Optional<StudentDTO> studentOptional = studentService.getStudentById(studentId);
                    studentGrades.put(studentId, grade);

                }
            }
            model.addAttribute("students", students);
            model.addAttribute("studentGrades", studentGrades);
            model.addAttribute("teacher", teacherService.getTeacherById(id).get());
            return "teachers/add_assignment"; // JSP page for editing assignment and grades
        } else {
            return "redirect:/teachers/" + id + "/courses/" + courseId; // Redirect if course or assignment is not found or user is unauthorized
        }
    }

    @PostMapping("/{id}/courses/{courseId}/assignment/{assignmentId}/edit")
    public String updateAssignment(@PathVariable("id") UUID id,
                                   @PathVariable("courseId") UUID courseId,
                                   @PathVariable("assignmentId") UUID assignmentId,
                                   @RequestParam Map<String, String> params,
                                   RedirectAttributes redirectAttributes) {


        Optional<CourseDTO> courseDTO = courseService.getCourseById(courseId);
        Optional<AssignmentDTO> assignmentDTO = assignmentService.getAssignmentById(assignmentId);

        if (courseDTO.isPresent() && assignmentDTO.isPresent() && courseDTO.get().getTeacher().getId().equals(id)) {
            try {
                // Update Assignment details
                AssignmentDTO updatedAssignment = assignmentDTO.get();
                updatedAssignment.setTitle(params.get("title"));
                updatedAssignment.setDescription(params.get("description"));
                updatedAssignment.setCoefficient(Double.parseDouble(params.get("coefficient")));
                assignmentService.updateAssignment(updatedAssignment);


                Gmailer gmailer = new Gmailer();

                // Update Student Grades
                for (Map.Entry<String, String> entry : params.entrySet()) {

                    if (entry.getKey().startsWith("grade_")) {

                        String studentIdStr = entry.getKey().substring(6); // Extraire l'ID de l'étudiant à partir de la clé
                        UUID studentId = UUID.fromString(studentIdStr); // Convertir en UUID
                        Double gradeValue = Double.parseDouble(entry.getValue()); // Récupérer la note

                        Optional<EnrollmentDTO> enrollmentOptional = enrollmentService.getEnrollmentByStudentIdAndCourseId(studentId, courseId);
                        if (enrollmentOptional.isPresent()) {
                            // Récupérer l'étudiant pour obtenir son email
                            Optional<StudentDTO> studentOpt = studentService.getStudentById(studentId);
                            if (studentOpt.isPresent()) {
                                String studentEmail = studentOpt.get().getEmail(); // Récupérer l'email de l'étudiant

                                // Pour déboguer ou envoyer un mail par la suite
                                System.out.println("L'email de l'étudiant est : " + studentEmail); // Affiche l'email dans la console

                                Optional<GradeDTO> grade = gradeService.getAllGradesByAssignmentIdAndEnrollmentId(assignmentId, enrollmentOptional.get().getId());
                                GradeDTO gradeDTO;
                                if (grade.isPresent()) {
                                    gradeDTO = grade.get();
                                    gradeDTO.setAssignmentId(assignmentId);
                                    gradeDTO.setScore(gradeValue);
                                    gradeDTO.setEnrollmentId(enrollmentOptional.get().getId());
                                    gradeService.updateGrade(gradeDTO);



                                    // TODO: Envoi d'un mail de modification de note à l'étudiant (avec studentEmail)

                                    gmailer.sendMail("Modification de votre note de contrôle",
                                            "Bonjour " + studentOpt.get().getFirstname() + ",\n\n" +
                                                    "Nous vous informons que la note que vous avez reçue pour le devoir intitulé '" + updatedAssignment.getTitle() + "' a été modifiée.\n\n" +
                                                    "Vous pouvez consulter la mise à jour dans votre espace étudiant.\n\n" +
                                                    "Cordialement,\n" +
                                                    "L'équipe pédagogique de " + courseDTO.get().getName(),studentEmail);

                                } else {


                                    System.out.println("jgn erojbenq bêriqnbreiqpob,njjZOIPK?FZeoipvj,iopkZVNZIOKBNZipbnZ¨POBZe");


                                    gradeDTO = new GradeDTO();
                                    gradeDTO.setAssignmentId(assignmentId);
                                    gradeDTO.setScore(gradeValue);
                                    gradeDTO.setEnrollmentId(enrollmentOptional.get().getId());
                                    gradeService.addGrade(gradeDTO);


                                }

                            }

                        } else {
                            throw new RuntimeException("Inscription non trouvée pour l'étudiant avec ID: " + studentId);
                        }
                    }
                }

                redirectAttributes.addFlashAttribute("success", "Le devoir et les notes ont été mis à jour avec succès.");
            } catch (Exception ex) {
                redirectAttributes.addFlashAttribute("error", "Erreur lors de la mise à jour du devoir et des notes : " + ex.getMessage());
            }
            return "redirect:/teachers/" + id + "/courses/" + courseId;
        } else {
            redirectAttributes.addFlashAttribute("error", "Vous n'êtes pas autorisé à mettre à jour ce devoir.");
            return "redirect:/teachers/" + id;
        }
    }


}
