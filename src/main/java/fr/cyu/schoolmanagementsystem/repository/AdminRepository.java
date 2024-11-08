package fr.cyu.schoolmanagementsystem.repository;

import fr.cyu.schoolmanagementsystem.model.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {
    Optional<Admin> findById(UUID id);

    Optional<Admin> findByEmail(String email);
}
