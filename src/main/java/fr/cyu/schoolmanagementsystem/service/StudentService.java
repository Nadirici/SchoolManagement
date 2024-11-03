package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.model.dto.StudentDTO;
import fr.cyu.schoolmanagementsystem.model.entity.Student;
import fr.cyu.schoolmanagementsystem.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final ModelMapper mapper;

    @Autowired
    public StudentService(StudentRepository studentRepository, ModelMapper mapper) {
        this.studentRepository = studentRepository;
        this.mapper = mapper;
    }

    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll().stream().map(this::mapToStudentDTO).toList();
    }

    public Optional<StudentDTO> getStudentById(UUID id) {
        return studentRepository.findById(id).map(this::mapToStudentDTO);
    }

    public UUID addStudent(StudentDTO studentDTO) {
        if (studentRepository.findByEmail(studentDTO.getEmail()).isPresent()) {
            throw new RuntimeException("A student with this email already exists.");
        }
        Student student = mapper.map(studentDTO, Student.class);
        studentRepository.save(student);
        return student.getId();
    }

    public void deleteStudentById(UUID id) {
        if (studentRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Student with this id does not exist.");
        }
        studentRepository.deleteById(id);
    }

    public Student findStudentById(UUID id) {
        return studentRepository.findById(id).orElse(null);
    }

    private StudentDTO mapToStudentDTO(Student student) {
        return mapper.map(student, StudentDTO.class);
    }
}
