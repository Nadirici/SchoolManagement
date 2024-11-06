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

    public UUID enrollStudent(EnrollmentDTO enrollmentDTO) {
        Student studentToMap = studentService.findStudentById(enrollmentDTO.getStudentId());
        Course courseToMap = courseService.findCourseById(enrollmentDTO.getCourseId());

        Enrollment enrollment = mapper.map(enrollmentDTO, Enrollment.class);

        enrollment.setStudent(studentToMap);
        enrollment.setCourse(courseToMap);

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

    private EnrollmentDTO mapToEnrollmentDTO(Enrollment enrollment) {
        return mapper.map(enrollment, EnrollmentDTO.class);
    }

    public Enrollment findEnrollmentById(UUID enrollmentId) {
        return enrollmentRepository.findById(enrollmentId).orElse(null);
    }

    public Optional<EnrollmentDTO> getEnrollmentByStudentIdAndCourseId(UUID studentId, UUID courseId) {
        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId).map(this::mapToEnrollmentDTO);
    }

}
