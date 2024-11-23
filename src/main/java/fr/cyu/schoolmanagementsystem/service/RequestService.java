package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.dao.GenericDAO;
import fr.cyu.schoolmanagementsystem.dao.RegistrationRequestDAO;
import fr.cyu.schoolmanagementsystem.dao.StudentDAO;
import fr.cyu.schoolmanagementsystem.dao.TeacherDAO;
import fr.cyu.schoolmanagementsystem.entity.Enrollment;
import fr.cyu.schoolmanagementsystem.entity.RegistrationRequest;
import fr.cyu.schoolmanagementsystem.entity.Student;
import fr.cyu.schoolmanagementsystem.entity.Teacher;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RequestService extends GenericServiceImpl<RegistrationRequest> {

    private final StudentService studentService;
    private final TeacherService teacherService;

    public RequestService(GenericDAO<RegistrationRequest> dao, EnrollmentService enrollmentService,GradeService gradeService) {
        super(dao);
        studentService = new StudentService(new StudentDAO(Student.class),enrollmentService,gradeService);
        teacherService = new TeacherService(new TeacherDAO(Teacher.class));
    }

    @Override
    protected UUID getEntityId(RegistrationRequest request) {
        return request.getId();
    }

    public List<RegistrationRequest> getPendingTeacherRequests() {
        return ((RegistrationRequestDAO) dao).findByTeacherIdIsNotNullAndStudentIdIsNullAndStatusFalse();
    }

    public List<RegistrationRequest> getPendingStudentRequests() {
        return ((RegistrationRequestDAO) dao).findByStudentIdIsNotNullAndTeacherIdIsNullAndStatusFalse();
    }

    public void approveRequest(UUID requestId) {
        try {
            Optional<RegistrationRequest> optionalRequest = dao.findById(requestId);

            if (optionalRequest.isPresent()) {
                RegistrationRequest request = optionalRequest.get();

                if (request.getStudent() != null) {
                    Student student = request.getStudent();
                    student.setVerified(true);
                    studentService.update(student);
                } else if (request.getTeacher() != null) {
                    Teacher teacher = request.getTeacher();
                    teacher.setVerified(true);
                    teacherService.update(teacher);
                } else {
                    throw new RuntimeException("Type de demandeur inconnu pour id :" + requestId);
                }

                request.setStatus(true);
                dao.update(request);
            }
        } catch (Exception e) {
            throw new RuntimeException("Demande non trouvée avec id: " + requestId, e);
        }
    }

    public void rejectRequest(UUID requestId) {
        try {
            Optional<RegistrationRequest> optionalRequest = ((RegistrationRequestDAO) dao).findById(requestId);
            if (optionalRequest.isPresent()) {
                dao.delete(optionalRequest.get());
            } else {
                throw new RuntimeException("Type de demandeur inconnu pour id :" + requestId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Demande non trouve avec id: " + requestId, e);
        }
    }
}
