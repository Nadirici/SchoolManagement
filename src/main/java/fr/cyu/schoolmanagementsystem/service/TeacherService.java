package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.model.entity.Course;
import fr.cyu.schoolmanagementsystem.repository.*;
import org.springframework.ui.Model;

import fr.cyu.schoolmanagementsystem.model.dto.CourseDTO;
import fr.cyu.schoolmanagementsystem.model.dto.TeacherDTO;

import fr.cyu.schoolmanagementsystem.model.entity.Teacher;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    private final RequestRepository requestRepository;
    private final CourseRepository courseRepository;


    private final ModelMapper mapper;
    private final AssignmentRepository assignmentRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, RequestRepository requestRepository, CourseRepository courseRepository,  ModelMapper mapper, AssignmentRepository assignmentRepository) {
        this.teacherRepository = teacherRepository;
        this.requestRepository = requestRepository;
        this.courseRepository = courseRepository;


        this.mapper = mapper;
        this.assignmentRepository = assignmentRepository;
    }

    public List<Teacher> getTeachersByDepartment(String department) {
        return teacherRepository.findByDepartment(department);
    }

    public List<TeacherDTO> getAllVerifiedTeachers() {
        return teacherRepository.findAllByIsVerified(true).stream().map(this::mapToTeacherDTO).toList();
    }



    @Transactional
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream().map(this::mapToTeacherDTO).toList();
    }
    @Transactional
    public Optional<TeacherDTO> getTeacherById(UUID id) {
        return teacherRepository.findById(id).map(this::mapToTeacherDTO);
    }

    public UUID addTeacher(TeacherDTO teacherDTO) {
        if (teacherRepository.findByEmail(teacherDTO.getEmail()).isPresent()) {
            throw new RuntimeException("A teacher with this email already exists.");
        }
        Teacher teacher = mapper.map(teacherDTO, Teacher.class);
        teacherRepository.save(teacher);
        return teacher.getId();
    }
    @Transactional
    public void deleteTeacherById(UUID id) {
        // Vérifier si l'enseignant existe
        if (!teacherRepository.existsById(id)) {
            throw new RuntimeException("Teacher with this id does not exist.");
        }

        // Supprimer les requests associées à cet enseignant
        requestRepository.deleteByTeacherId(id);

        // Supprimer les cours associés à cet enseignant
        List<Course> courses = courseRepository.findByTeacherId(id);
        for (Course course : courses) {
            // Supprimer les notes associées à chaque cours
            assignmentRepository.deleteByCourseId(course.getId());
        }

        // Supprimer les cours
        courseRepository.deleteByTeacherId(id);

        // Supprimer l'enseignant
        teacherRepository.deleteById(id);
    }

    public void updateTeacher(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    public boolean assignTeacherToCourse(UUID teacherId, UUID courseId) {
        // TODO: Implementing logic and RuntimeException
        return false;
    }

    private TeacherDTO mapToTeacherDTO(Teacher teacher) {
        return mapper.map(teacher, TeacherDTO.class);
    }

    public Optional<Teacher> getTeacherByEmail(String email) {
        return teacherRepository.findByEmail(email);
    }

    public void registerTeacher(Teacher teacher) {
        teacherRepository.save(teacher);
    }
}
