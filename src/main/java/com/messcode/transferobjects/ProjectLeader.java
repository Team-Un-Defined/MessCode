package com.messcode.transferobjects;

public class ProjectLeader extends Employee {
    ProjectLeader(String name, String surname, String email, byte[] hashedPassword, char salt) {
        super(name, surname, email, hashedPassword, salt);
    }
}
