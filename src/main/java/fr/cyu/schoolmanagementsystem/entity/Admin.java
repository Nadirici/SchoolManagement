package fr.cyu.schoolmanagementsystem.entity;

import fr.cyu.schoolmanagementsystem.entity.base.BasePersonEntity;
import fr.cyu.schoolmanagementsystem.util.HashPassword;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Base64;

@Entity
@Table(name = "admins")
public class Admin extends BasePersonEntity {

    public Admin() {
        super();
    }

    public Admin(String salt) {
        super("admin","admin","admin@admin.com", HashPassword.hashPassword("admin",Base64.getDecoder().decode(salt)),Base64.getEncoder().encodeToString(salt.getBytes()), true);
    }
}
