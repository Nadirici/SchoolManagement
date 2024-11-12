package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.model.dto.CourseDTO;
import fr.cyu.schoolmanagementsystem.model.dto.EnrollmentDTO;
import fr.cyu.schoolmanagementsystem.model.dto.StudentDTO;
import fr.cyu.schoolmanagementsystem.model.entity.Course;
import fr.cyu.schoolmanagementsystem.model.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.model.entity.Student;
import fr.cyu.schoolmanagementsystem.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    private final StudentService studentService;

    private final CourseService courseService;

    private final ModelMapper mapper;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, StudentService studentService, CourseService courseService, ModelMapper mapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentService = studentService;
        this.courseService = courseService;
        this.mapper = mapper;
    }

    public List<EnrollmentDTO> getAllEnrollments() {
        return enrollmentRepository.findAll().stream().map(this::mapToEnrollmentDTO).toList();
    }

    public Optional<EnrollmentDTO> getEnrollmentById(UUID enrollmentId) {
        return enrollmentRepository.findById(enrollmentId).map(this::mapToEnrollmentDTO);
    }

    public UUID enrollStudentToCourse(EnrollmentDTO enrollmentDTO) {
        Optional<StudentDTO> student = studentService.getStudentById(enrollmentDTO.getStudentId());
        Optional<CourseDTO> course = courseService.getCourseById(enrollmentDTO.getCourseId());

        if (student.isEmpty() || course.isEmpty()) {
            throw new RuntimeException("Can't enroll because Student or Course not found");
        }

        Enrollment enrollment = mapper.map(enrollmentDTO, Enrollment.class);

        Student s = mapper.map(student.get(), Student.class);
        Course c = mapper.map(course.get(), Course.class);

        enrollment.setStudent(s);
        enrollment.setCourse(c);

        enrollmentRepository.save(enrollment);

        return enrollment.getId();
    }

    public void deleteEnrollment(UUID enrollmentId) {
        enrollmentRepository.deleteById(enrollmentId);
    }

    @Transactional
    public List<CourseDTO> getCoursesForStudent(UUID studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return enrollments.stream()
                .map(enrollment -> mapper.map(enrollment.getCourse(), CourseDTO.class))
                .toList();
    }

    @Transactional
    public List<StudentDTO> getStudentsForCourse(UUID courseId) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        return enrollments.stream()
                .map(enrollment -> mapper.map(enrollment.getStudent(), StudentDTO.class))
                .toList();
    }

    public Optional<EnrollmentDTO> getEnrollmentByStudentIdAndCourseId(UUID studentId, UUID courseId) {
        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId).map(this::mapToEnrollmentDTO);
    }

    public List<EnrollmentDTO> getEnrollmentsByStudentId(UUID studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream().map(this::mapToEnrollmentDTO).toList();
    }

    public List<EnrollmentDTO> getEnrollmentsByCourseId(UUID courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream().map(this::mapToEnrollmentDTO).toList();
    }

    private EnrollmentDTO mapToEnrollmentDTO(Enrollment enrollment) {
        return mapper.map(enrollment, EnrollmentDTO.class);
    }
}
