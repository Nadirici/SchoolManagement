package servlets.schoolmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import servlets.schoolmanagement.models.entity.RegistrationRequest;
import servlets.schoolmanagement.models.entity.Student;
import servlets.schoolmanagement.models.entity.Teacher;
import servlets.schoolmanagement.repository.RequestRepository;

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
        return requestRepository.findByTeacherIdIsNotNullAndStudentIdIsNullAndStatutFalse();
    }

    public List<RegistrationRequest> getPendingStudentRequests() {
        return requestRepository.findByStudentIdIsNotNullAndTeacherIdIsNullAndStatutFalse();
    }
    public void saveRequest(RegistrationRequest request) {
        // S'assurer que l'ID est assigné avant la persistance
        if (request.getId() == null || request.getId().isEmpty()) {
            request.setId(UUID.randomUUID().toString());
        }
        requestRepository.save(request) ; // Enregistre la demande dans la base de données
    }

    // Méthode pour approuver une demande
    @Transactional
    public void approveRegistrationRequest(String requestId) {
        Optional<RegistrationRequest> optionalRequest = requestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            RegistrationRequest request = optionalRequest.get();
            Student student = null;
            Teacher teacher = null;
            if(request.getStudent() != null) {
                student = request.getStudent();
            }
            else {
                // TO-DO : Implémenter teacher
                teacher = request.getTeacher();
            }
            
            // Vérifie si l'ID commence par '2' (étudiant) ou '1' (professeur)
            if (student != null) {
                student.setVerified(true);
                // Inscription de l'étudiant
                studentService.registerStudent(student);
            } else if (teacher != null) {
                // Inscription de l'enseignant
                teacher.setVerified(true);
                teacherService.registerTeacher(teacher);
            } else {
                throw new RuntimeException("Type de demandeur inconnu pour l'ID : "+requestId);
            }
            // Mise à jour du statut de la demande comme "approuvée"
            request.setStatut(true);
            requestRepository.save(request);
        } else {
            throw new RuntimeException("Demande non trouvée avec l'ID : " + requestId);
        }
    }

    // Méthode pour rejeter une demande
    @Transactional
    public void rejectRegistrationRequest(String requestId) {
        Optional<RegistrationRequest> optionalRequest = requestRepository.findById(requestId);
        if (optionalRequest.isPresent()) {
            requestRepository.delete(optionalRequest.get()); // Supprime la demande de la BDD
        } else {
            throw new RuntimeException("Demande non trouvée avec l'ID : " + requestId);
        }
    }
}
