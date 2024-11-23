package fr.cyu.schoolmanagementsystem.service;

import fr.cyu.schoolmanagementsystem.dao.RegistrationRequestDAO;
import fr.cyu.schoolmanagementsystem.entity.RegistrationRequest;
import fr.cyu.schoolmanagementsystem.entity.Student;
import fr.cyu.schoolmanagementsystem.entity.Teacher;

public class RegistrationRequestService {

    private final RegistrationRequestDAO registrationRequestDAO;

    // Constructeur pour initialiser le DAO
    public RegistrationRequestService(RegistrationRequestDAO registrationRequestDAO) {
        this.registrationRequestDAO = registrationRequestDAO;
    }

    /**
     * Méthode pour enregistrer une demande d'enregistrement pour un étudiant ou un enseignant
     * @param user L'étudiant ou l'enseignant pour lequel la demande est créée
     */
    public void saveRequest(Object user) throws NullPointerException {
        RegistrationRequest request = null;
        if(user instanceof Student ) {
            request = new RegistrationRequest((Student) user);
        }
        else if(user instanceof Teacher ) {
            request = new RegistrationRequest((Teacher) user);
        }
        if(request != null) {
            registrationRequestDAO.save(request);
        }
        else {
            throw new NullPointerException("Registration Request is null");
        }
    }
}
