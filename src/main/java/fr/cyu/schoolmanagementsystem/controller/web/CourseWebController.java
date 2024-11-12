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

import java.util.*;

@Controller

public class CourseWebController {

    private final TeacherService teacherService;
    private final EnrollmentService enrollmentService;
    private final CourseService courseService;
    private final GradeService gradeService;
    private final AssignmentService assignmentService;

    private final AdminService adminService;

    @Autowired
    public CourseWebController(TeacherService teacherService, AdminService adminService, EnrollmentService enrollmentService, CourseService courseService, GradeService gradeService, AssignmentService assignmentService) {
        this.teacherService = teacherService;
        this.enrollmentService = enrollmentService;
        this.courseService = courseService;
        this.gradeService = gradeService;
        this.assignmentService = assignmentService;
        this.adminService = adminService;
    }

    @RequestMapping(value = {"/*/{id}/courses/{courseId}"}, method = RequestMethod.GET)
    public String showCourseDashboard(@PathVariable("id") UUID id, @PathVariable("courseId") UUID courseId, Model model, RedirectAttributes redirectAttributes) {

        Optional<CourseDTO> course = courseService.getCourseById(courseId);
        if (course.isPresent()) {
                List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
                boolean isAssignedTeacher= course.get().getTeacher().getId().equals(id);
                boolean isAdmin= adminService.isAdmin(id);
                boolean isEnrolledStudent = enrollments.stream().anyMatch(enrollment -> enrollment.getStudentId().equals(id));
            if (isAdmin || isAssignedTeacher || isEnrolledStudent) {
                // Get All Assignment related to the course and get grades associated
                List<AssignmentDTO> assignments = assignmentService.getAllAssignmentsByCourseId(courseId);

                Map<UUID, Double> average_grades = new HashMap<>();
                Map<UUID, Double> min_grade = new HashMap<>();
                Map<UUID, Double> max_grade = new HashMap<>();
                Map<UUID, Double> studentAssignmentGrades = new HashMap<>();
                for (AssignmentDTO assignment : assignments) {
                    double grade = gradeService.calculateAverageGradeForAssignment(assignment.getId());
                    double minGrade = gradeService.getMinGradeForAssignment(assignment.getId());
                    double maxGrade = gradeService.getMaxGradeForAssignment(assignment.getId());
                    average_grades.put(assignment.getId(), grade);
                    min_grade.put(assignment.getId(), minGrade);
                    max_grade.put(assignment.getId(), maxGrade);

                }
                if (isEnrolledStudent){
                    Optional<EnrollmentDTO> enrollment = enrollmentService.getEnrollmentByStudentIdAndCourseId(id, courseId);
                    for (AssignmentDTO assignment : assignments) {
                        Optional<GradeDTO> studentGrade = gradeService.getAllGradesByAssignmentIdAndEnrollmentId(assignment.getId(), enrollment.get().getId());
                        studentAssignmentGrades.put(assignment.getId(), studentGrade.orElse(null).getScore());}

                }
                model.addAttribute("studentAssignmentGrades", studentAssignmentGrades);

                model.addAttribute("assignments", assignments);
                model.addAttribute("averageGrades", average_grades);
                model.addAttribute("minGrade", min_grade);
                model.addAttribute("maxGrade", max_grade);

            }
            model.addAttribute("canViewDetails", isAdmin || isAssignedTeacher || isEnrolledStudent);
            model.addAttribute("isAssignedTeacher", isAssignedTeacher);
            model.addAttribute("isEnrolledStudent", isEnrolledStudent);
            model.addAttribute("course", course.get());
            return "students/single-course";
        } else {
            redirectAttributes.addFlashAttribute("error", "Enrollment not found");
            return "redirect:/students"; // Redirect to the list page if student not found
        }
    }


    @RequestMapping(value = {"/*/{id}/courses/{courseId}/grades"}, method = RequestMethod.GET)
    public String showCourseGrades(@PathVariable("id") UUID id, @PathVariable("courseId") UUID courseId, Model model, RedirectAttributes redirectAttributes) {

        Optional<CourseDTO> course = courseService.getCourseById(courseId);
        List<StudentDTO> students= enrollmentService.getStudentsForCourse(courseId);
        List<AssignmentDTO> assignments = assignmentService.getAllAssignmentsByCourseId(courseId);

        boolean isAssignedTeacher= course.get().getTeacher().getId().equals(id);
        boolean isAdmin= adminService.isAdmin(id);
        if (course.isPresent()) {
            if (isAdmin || isAssignedTeacher) {
                List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
                Map<UUID, Double> averageGrades = new HashMap<>();
                Map<UUID, Map<UUID, Double>> assignmentsGrades = new HashMap<>();

                for (EnrollmentDTO enrollment : enrollments) {
                    Map<UUID, Double> studentCourseGrades = new HashMap<>();
                    double average = gradeService.calculateAverageGradeForEnrollment(enrollment.getId());
                    List<GradeDTO> grades = gradeService.getAllGradesByEnrollmentId(enrollment.getId());
                    for(GradeDTO grade: grades){
                        studentCourseGrades.put(grade.getAssignmentId(), grade.getScore());
                    }
                    assignmentsGrades.put(enrollment.getStudentId(), studentCourseGrades);
                    averageGrades.put(enrollment.getStudentId(),average);
                }


                // Ajouter les informations au modèle

                model.addAttribute("assignmentsGrades", assignmentsGrades);
                model.addAttribute("averageGrades", averageGrades);
            }
            model.addAttribute("assignments", assignments);
            model.addAttribute("students", students);
            model.addAttribute("canViewGrades", isAdmin || isAssignedTeacher);
            model.addAttribute("course", course.get());
            return "lists/course_grades";
        } else {
            redirectAttributes.addFlashAttribute("error", "Enrollment not found");
            return "redirect:/students/single-course"; // Redirect to the list page if student not found
        }
    }




}
