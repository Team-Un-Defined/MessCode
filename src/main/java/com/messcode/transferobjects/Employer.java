package com.messcode.transferobjects;

public class Employer extends Superuser {
    Employer(String name, String surname, String email, byte[] hashedPassword, char salt) {
        super(name, surname, email, hashedPassword, salt);
    }
}
