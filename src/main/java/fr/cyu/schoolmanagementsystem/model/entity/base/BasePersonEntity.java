package fr.cyu.schoolmanagementsystem.model.entity.base;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public abstract class BasePersonEntity extends BaseEntity {



    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;

    private String salt;

    private String password;

    @Column(nullable = false, name = "is_verified")
    private boolean isVerified;

    public BasePersonEntity() {
        super();
    }

    public BasePersonEntity(String firstname, String lastname, String email, String password, String salt) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.isVerified = false;
    }

}
