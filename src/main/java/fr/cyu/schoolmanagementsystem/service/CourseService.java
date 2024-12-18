package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.model.dto.CourseDTO;
import fr.cyu.schoolmanagementsystem.model.dto.StudentDTO;
import fr.cyu.schoolmanagementsystem.model.dto.TeacherDTO;
import fr.cyu.schoolmanagementsystem.model.entity.Course;
import fr.cyu.schoolmanagementsystem.model.entity.Teacher;
import fr.cyu.schoolmanagementsystem.model.entity.enumeration.Departement;
import fr.cyu.schoolmanagementsystem.repository.CourseRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

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

    public Optional<Course> getCourseByName(String name) {
        return courseRepository.findByName(name);
    }





    @Transactional
    public Optional<CourseDTO> getCourseById(UUID id) {
        return courseRepository.findById(id).map(this::mapsToCourseDTO);
    }

    @Transactional
    public Optional<Course> getCoursebById(UUID id) {
        return courseRepository.findById(id);
    }

    @Transactional
    public UUID update(CourseDTO courseDTO) {
        // Récupérer le cours existant à partir de l'ID
        Optional<Course> existingCourse = courseRepository.findById(courseDTO.getId());

        if (existingCourse.isEmpty()) {
            throw new RuntimeException("Course with this id does not exist.");
        }

        // Mapper le cours existant et les nouvelles données
        Course course = existingCourse.get();

        // Mettre à jour le nom et la description du cours
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());

        // Récupérer l'enseignant à partir de l'ID dans le DTO
        Optional<TeacherDTO> teacherDTO = teacherService.getTeacherById(courseDTO.getTeacher().getId());

        if (teacherDTO.isEmpty()) {
            throw new RuntimeException("Teacher with this id does not exist.");
        }

        // Mapper l'enseignant
        Teacher teacher = mapper.map(teacherDTO.get(), Teacher.class);
        course.setTeacher(teacher);

        // Mettre à jour les autres propriétés du cours si nécessaire
        course.setDayOfWeek(courseDTO.getDayOfWeek());
        course.setStartTime(courseDTO.getStartTime());
        course.setEndTime(courseDTO.getEndTime());

        // Sauvegarder les modifications
        courseRepository.save(course);

        // Retourner l'ID du cours mis à jour
        return course.getId();
    }


    public UUID addCourse(CourseDTO courseDTO) {
        if (courseRepository.findByName(courseDTO.getName()).isPresent()) {
            throw new RuntimeException("A course with this name already exists.");
        }

        Optional<TeacherDTO> teacher = teacherService.getTeacherById(courseDTO.getTeacher().getId());

        if (teacher.isEmpty()) {
            throw new RuntimeException("A teacher with this id does not exist.");
        }

        Course course = mapper.map(courseDTO, Course.class);

        Teacher t = mapper.map(teacher.get(), Teacher.class);

        course.setTeacher(t);

        courseRepository.save(course);

        return course.getId();
    }

    public void deleteCourse(UUID courseId) {
        if (courseRepository.findById(courseId).isEmpty()) {
            throw new RuntimeException("Course with this id does not exist.");
        }
        courseRepository.deleteById(courseId);
    }

    public UUID updateCourse(CourseDTO courseDTO) {
        // TODO: Implementing logic and RuntimeException if needed
        return null;
    }

    public List<StudentDTO> getStudentsInCourse(UUID courseId) {
        // TODO: Implementing logic and RuntimeException if needed
        return null;
    }

    @Transactional
    public List<CourseDTO> getCoursesByTeacherId(UUID teacherId) {
        List<Course> courses = courseRepository.findByTeacherId(teacherId);

        return courses.stream()
                .map(course -> mapper.map(course, CourseDTO.class))
                .toList();
    }


    public List<CourseDTO> getCoursesInDepartment(String department){
        Map<String, List<Teacher>> teachersByDepartment = Arrays.stream(Departement.values())
                .collect(Collectors.toMap(Departement::getName, dept -> teacherService.getTeachersByDepartment(dept.getName())));
        List<Teacher> teachersInDepartment = teachersByDepartment.get(department);
        if (teachersInDepartment == null) {
            return null;
        }

        return teachersInDepartment.stream()
                .flatMap(teacher -> this.getCoursesByTeacherId(teacher.getId()).stream())
                .toList();
    }


    private CourseDTO mapsToCourseDTO(Course course) {
        return mapper.map(course, CourseDTO.class);
    }


}
