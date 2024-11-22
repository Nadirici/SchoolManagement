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
                return "redirect:../"+id+"/courses"; // Redirection vers la page du cours si l'accès est refusé
            }

            model.addAttribute("assignments", assignments);
            model.addAttribute("students", students);
            model.addAttribute("canViewGrades", isAdmin || isAssignedTeacher);


            model.addAttribute("course", course.get());
            if (isAssignedTeacher){
                model.addAttribute("teacher", teacherService.getTeacherById(id).get());
            }else{
                model.addAttribute("admin", adminService.getAdmin(id));
            }
            return "courses/course_grades_table";
        } else {
            redirectAttributes.addFlashAttribute("error", "Cours introuvable.");
            return "redirect:../"+ id+"/courses/"+courseId; // Redirection vers la page du cours si le cours est introuvable
        }
    }






}
