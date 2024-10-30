package servlets.schoolmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servlets.schoolmanagement.models.entity.RegistrationRequest;


import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<RegistrationRequest, String> {
    List<RegistrationRequest> findByStatutFalse(); // Récupère les demandes en attente (non approuvées)
    Optional<RegistrationRequest> findById(String id) ;
    List<RegistrationRequest> findByTeacherIdIsNotNullAndStudentIdIsNullAndStatutFalse();
    List<RegistrationRequest> findByStudentIdIsNotNullAndTeacherIdIsNullAndStatutFalse();

}
