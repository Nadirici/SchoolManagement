package fr.cyu.schoolmanagementsystem.service;


import fr.cyu.schoolmanagementsystem.model.entity.Admin;
import fr.cyu.schoolmanagementsystem.repository.AdminRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AdminService {

    private AdminRepository adminRepository;


    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }


    public Admin getAdmin(UUID id){
        Optional<Admin> adminOptional = adminRepository.findById(id);

        if (adminOptional.isEmpty()){
            throw new RuntimeException("Admin not found");
        }

        return adminOptional.get();
    }

    public Admin getAdminByEmail(String email) {
        Optional<Admin> admin = adminRepository.findByEmail(email);

        if (admin.isEmpty()){
            throw new RuntimeException("Admin not found");
        }

        return admin.get();
    }
    public boolean isAdmin(UUID userId) {
        return adminRepository.findById(userId).isPresent();
    }
}
