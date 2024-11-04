package servlets.schoolmanagement.models.base;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.sql.Date;
import java.util.UUID;


@Getter
@MappedSuperclass
public abstract class User {


    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "user_email")
    private String email;
    @Column(name = "user_password")
    private String password;
    @Column(name = "created_at")
    private java.sql.Date createdAt;
    @Column(name = "updated_at")
    private java.sql.Date updatedAt;

    public User(String firstName, String lastName, String email, String password) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.createdAt = new java.sql.Date(System.currentTimeMillis());
        this.updatedAt = new java.sql.Date(System.currentTimeMillis());
    }

    public User() {

    }

    // Méthodes d'audit pour gérer automatiquement les dates
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date(System.currentTimeMillis());
        this.updatedAt = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date(System.currentTimeMillis());
    }





}
