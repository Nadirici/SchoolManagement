package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.model.dto.CourseDTO;
import fr.cyu.schoolmanagementsystem.model.entity.Course;
import fr.cyu.schoolmanagementsystem.model.entity.Teacher;
import fr.cyu.schoolmanagementsystem.repository.CourseRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    private final TeacherService teacherService;

    private final ModelMapper mapper;

    @Autowired
    public CourseService(CourseRepository courseRepository, TeacherService teacherService, ModelMapper mapper) {
        this.courseRepository = courseRepository;
        this.teacherService = teacherService;
        this.mapper = mapper;
    }

    @Transactional
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream().map(this::mapsToCourseDTO).toList();
    }

    @Transactional
    public Optional<CourseDTO> getCourseById(UUID id) {
        return courseRepository.findById(id).map(this::mapsToCourseDTO);
    }

    public UUID addCourse(CourseDTO courseDTO) {
        if (courseRepository.findByName(courseDTO.getName()).isPresent()) {
            throw new RuntimeException("A course with this name already exists.");
        }

        Teacher teacherToMap = teacherService.findByTeacherId(courseDTO.getTeacher().getId());

        Course course = mapper.map(courseDTO, Course.class);

        course.setTeacher(teacherToMap);

        courseRepository.save(course);

        return course.getId();
    }

    public void deleteCourseById(UUID id) {
        if (courseRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Course with this id does not exist.");
        }
        courseRepository.deleteById(id);
    }

    public Course findCourseById(UUID id) {
        return courseRepository.findById(id).orElse(null);
    }

    private CourseDTO mapsToCourseDTO(Course course) {
        return mapper.map(course, CourseDTO.class);
    }


}
