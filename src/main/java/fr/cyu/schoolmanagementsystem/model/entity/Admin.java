package fr.cyu.schoolmanagementsystem.model.entity;

import fr.cyu.schoolmanagementsystem.model.entity.base.BasePersonEntity;
import fr.cyu.schoolmanagementsystem.model.passwordmanager.HashPassword;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Base64;

@Getter
@Setter
@Entity
@Table(name = "admins")
public class Admin extends BasePersonEntity {

    public Admin(String salt) {
        super("admin","admin","admin@admin.com",HashPassword.hashPassword("admin",Base64.getDecoder().decode(salt)),Base64.getEncoder().encodeToString(salt.getBytes()));
    }
    public Admin(){
        super();
    }


}
