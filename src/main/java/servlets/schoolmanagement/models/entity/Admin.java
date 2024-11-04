package servlets.schoolmanagement.models.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import servlets.schoolmanagement.models.base.User;

import java.util.UUID;

@Getter
@Setter
public class Admin extends User {

    private String id;

    private static Admin admin;
    private Admin() {
        super("admin","admin","admin@admin.com","admin");
        this.id="0"+UUID.randomUUID();
    }

    public static Admin getAdmin() {
        if (admin == null) {
            admin = new Admin();
        }
        return admin;
    }

    // Constructeurs, getters et setters
}
