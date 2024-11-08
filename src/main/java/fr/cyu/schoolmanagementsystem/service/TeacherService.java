package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.model.dto.TeacherDTO;
import fr.cyu.schoolmanagementsystem.model.entity.Student;
import fr.cyu.schoolmanagementsystem.model.entity.Teacher;
import fr.cyu.schoolmanagementsystem.repository.TeacherRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    private final ModelMapper mapper;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, ModelMapper mapper) {
        this.teacherRepository = teacherRepository;
        this.mapper = mapper;
    }

    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream().map(this::mapToTeacherDTO).toList();
    }

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

    public void deleteTeacherById(UUID id) {
        if (teacherRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Teacher with this id does not exist.");
        }
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
