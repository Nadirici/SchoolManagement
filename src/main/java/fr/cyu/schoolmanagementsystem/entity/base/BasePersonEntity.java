package fr.cyu.schoolmanagementsystem.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BasePersonEntity extends BaseEntity {

    private String firstname;

    private String lastname;

    private String email;

    private String salt;

    private String password;

    @Column(name = "is_verified")
    private boolean isVerified;

    public BasePersonEntity() {
        super();
    }

    public BasePersonEntity(String firstname, String lastname, String email, String password, String salt, boolean isVerified) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.salt = salt;
        this.password = password;
        this.isVerified = isVerified;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }
}
