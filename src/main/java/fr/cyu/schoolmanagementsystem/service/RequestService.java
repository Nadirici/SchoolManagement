package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.model.entity.RegistrationRequest;
import fr.cyu.schoolmanagementsystem.model.entity.Student;
import fr.cyu.schoolmanagementsystem.model.entity.Teacher;
import fr.cyu.schoolmanagementsystem.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final StudentService studentService;
    private final TeacherService teacherService;

    @Autowired
    public RequestService(RequestRepository requestRepository, StudentService studentService, TeacherService teacherService) {
        this.requestRepository = requestRepository;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    public List<RegistrationRequest> getPendingTeacherRequests() {
        return requestRepository.findByTeacherIdIsNotNullAndStudentIdIsNullAndStatusFalse();
    }

    public List<RegistrationRequest> getPendingStudentRequests() {
        return requestRepository.findByStudentIdIsNotNullAndTeacherIdIsNullAndStatusFalse();
    }
    public void saveRequest(RegistrationRequest request) {
        requestRepository.save(request) ; // Enregistre la demande dans la base de données
    }

    @Cacheable("pendingRequestsCount")
    public double getTotalPendingRequests() {
        return requestRepository.countByStatus(false);
    }


    // Méthode pour approuver une demande
    @Transactional
    public void approveRegistrationRequest(UUID requestId) {
        Optional<RegistrationRequest> optionalRequest = requestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RegistrationRequest request = optionalRequest.get();
            Student student = null;
            Teacher teacher = null;
            if(request.getStudent() != null) {
                student = request.getStudent();
            }
            else {
                teacher = request.getTeacher();
            }


            if (student != null) {
                student.setVerified(true);
                // Inscription de l'étudiant
                studentService.updateStudent(student);
            } else if (teacher != null) {
                // Inscription de l'enseignant
                teacher.setVerified(true);
                teacherService.updateTeacher(teacher);
            } else {
                throw new RuntimeException("Type de demandeur inconnu pour l'ID : "+requestId);
            }
            // Mise à jour du statut de la demande comme "approuvée"
            request.setStatus(true);
            requestRepository.save(request);
        } else {
            throw new RuntimeException("Demande non trouvée avec l'ID : " + requestId);
        }
    }

    // Méthode pour rejeter une demande
    @Transactional
    public void rejectRegistrationRequest(UUID requestId) {
        Optional<RegistrationRequest> optionalRequest = requestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            requestRepository.delete(optionalRequest.get()); // Supprime la demande de la BDD
        } else {
            throw new RuntimeException("Demande non trouvée avec l'ID : " + requestId);
        }
    }
}