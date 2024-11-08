package fr.cyu.schoolmanagementsystem.repository;

import fr.cyu.schoolmanagementsystem.model.entity.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<RegistrationRequest, UUID> {
    List<RegistrationRequest> findByStatusFalse(); // Récupère les demandes en attente (non approuvées)
    Optional<RegistrationRequest> findById(UUID id);
    List<RegistrationRequest> findByTeacherIdIsNotNullAndStudentIdIsNullAndStatusFalse();
    List<RegistrationRequest> findByStudentIdIsNotNullAndTeacherIdIsNullAndStatusFalse();

}