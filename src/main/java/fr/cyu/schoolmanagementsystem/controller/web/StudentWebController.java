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

    @GetMapping("/{id}")
    public String showStudentDashboard(@PathVariable("id") UUID studentId, Model model, RedirectAttributes redirectAttributes) {
        Optional<StudentDTO> student = studentService.getStudentById(studentId);

        if (student.isPresent()) {
            List<CourseDTO> courses = enrollmentService.getCoursesForStudent(studentId);
            model.addAttribute("student", student.get());
            model.addAttribute("courses", courses);
            return "students/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Student not found");
            return "redirect:/students"; // Redirect to the list page if student not found
        }
    }

    @GetMapping("/{studentId}/courses/{courseId}")
    public String showCourseDashboard(@PathVariable("studentId") UUID studentId, @PathVariable("courseId") UUID courseId, Model model, RedirectAttributes redirectAttributes) {
        Optional<EnrollmentDTO> enrollment = enrollmentService.getEnrollmentByStudentIdAndCourseId(studentId, courseId);
        if (enrollment.isPresent()) {
            Optional<CourseDTO> course = courseService.getCourseById(courseId);
            if (course.isPresent()) {
                // Get All Assignment related to the course and get grades associated
                List<AssignmentDTO> assignments = assignmentService.getAllAssignmentsByCourseId(courseId);

                Map<AssignmentDTO, GradeDTO> grades = new HashMap<>();

                for (AssignmentDTO assignment : assignments) {
                    Optional<GradeDTO> grade = gradeService.getAllGradesByAssignmentIdAndEnrollmentId(assignment.getId(), enrollment.get().getId());
                    grades.put(assignment, grade.orElse(null));
                }

                model.addAttribute("course", course.get());
                model.addAttribute("grades", grades);
                return "students/single-course";
            } else {
                redirectAttributes.addFlashAttribute("error", "Course not found");
                return "redirect:/students"; // Redirect to the list page if student not found
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Enrollment not found");
            return "redirect:/students"; // Redirect to the list page if student not found
        }
    }

}
