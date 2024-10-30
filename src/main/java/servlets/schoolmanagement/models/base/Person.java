package servlets.schoolmanagement.models.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public  abstract class Person extends User {

    @Column(name = "is_verified")
    private boolean isVerified;

    public Person(String firstName, String lastName, String email, String password, boolean isVerified,String id) {
        super(firstName, lastName, email, password,id);
        this.isVerified = isVerified;
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
