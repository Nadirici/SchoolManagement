package servlets.schoolmanagement.models.base;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.sql.Date;


@MappedSuperclass
public abstract class User {

    @Id
    @NonNull
    @Column(name = "user_id")
    private String id;
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

    public String getEmail() {
        return email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }



}
