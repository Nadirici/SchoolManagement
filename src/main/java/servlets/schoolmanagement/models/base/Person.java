package servlets.schoolmanagement.models.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public  abstract class Person extends User {

    @Column(name = "is_verified")
    private boolean isVerified;
    private String salt;

    public Person(String firstName, String lastName, String email, String password, boolean isVerified, String salt) {
        super(firstName, lastName, email, password);
        this.isVerified = isVerified;
        this.salt = salt;
    }

    public Person() {
        super();
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
