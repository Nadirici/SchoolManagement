package servlets.schoolmanagement.models.entity;

import servlets.schoolmanagement.models.base.User;

import java.util.UUID;

public class Admin extends User {

    public Admin() {
        super("admin","admin","admin@admin.com","admin","0" + UUID.randomUUID());
    }


}
