package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.model.dto.CourseDTO;
import fr.cyu.schoolmanagementsystem.model.dto.StudentDTO;
import fr.cyu.schoolmanagementsystem.model.entity.Course;
import fr.cyu.schoolmanagementsystem.model.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.model.entity.Student;
import fr.cyu.schoolmanagementsystem.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final ModelMapper mapper;

    private final GradeRepository gradeRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final RequestRepository requestRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, ModelMapper mapper, GradeRepository gradeRepository, EnrollmentRepository enrollmentRepository, RequestRepository requestRepository) {
        this.studentRepository = studentRepository;
        this.mapper = mapper;
        this.gradeRepository = gradeRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.requestRepository = requestRepository;
    }

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream().map(this::mapToStudentDTO).toList();
    }

    public Optional<StudentDTO> getStudentById(UUID id) {
        return studentRepository.findById(id).map(this::mapToStudentDTO);
    }




    @Cacheable("verifiedStudentsCount")
    public double getVerifiedStudentCount() {
        return studentRepository.countByIsVerified(true);
    }

    public UUID addStudent(StudentDTO studentDTO) {
        if (studentRepository.findByEmail(studentDTO.getEmail()).isPresent()) {
            throw new RuntimeException("A student with this email already exists.");
        }
        Student student = mapper.map(studentDTO, Student.class);
        studentRepository.save(student);
        return student.getId();
    }

    @Transactional
    public void deleteStudent(UUID id) {
        // Vérifier si l'étudiant existe
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student with this id does not exist.");
        }

        // Supprimer toutes les demandes associées à cet étudiant
        requestRepository.deleteByStudentId(id);

        // Récupérer toutes les inscriptions de l'étudiant
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(id);

        // Supprimer toutes les notes associées aux inscriptions de cet étudiant
        for (Enrollment enrollment : enrollments) {
            gradeRepository.deleteByEnrollmentId(enrollment.getId());
        }

        // Supprimer les inscriptions
        enrollmentRepository.deleteByStudentId(id);

        // Supprimer l'étudiant
        studentRepository.deleteById(id);
    }

    public void updateStudent(Student student) {
        studentRepository.save(student);
    }

    public List<CourseDTO> getCoursesByStudentId(UUID studentId) {
        // TODO: Implementing logic and RuntimeException
        return null;
    }

    public List<StudentDTO> searchStudents(String query) {
        // TODO: Implementing logic and RuntimeException
        return null;
    }

    private StudentDTO mapToStudentDTO(Student student) {
        return mapper.map(student, StudentDTO.class);
    }

    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    public void registerStudent(Student student) {
        studentRepository.save(student);
    }
}
