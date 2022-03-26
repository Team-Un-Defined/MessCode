package com.messcode.transferobjects;

import java.security.NoSuchAlgorithmException;

public class Employee extends User {
    int id;
    private String type = "employee";
    String name;
    String surname;
    String email;
    private byte[] hashedPassword;
    private String salt;

    // if you are creating new employee you use this constructor
    Employee(int id, String name, String surname, String email, String password) throws NoSuchAlgorithmException {
        super(name + " " + surname);
        AccountManager myAccountManager = new AccountManager();
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.salt = myAccountManager.generateSalt();
        this.hashedPassword = myAccountManager.hashPassword(password, salt);
    }

    // if you took employee from database you use this constructor
    Employee(int id, String name, String surname, String email, byte[] hashedPassword, String salt) {
        super(name + " " + surname);
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
    }

    public boolean isEmployee() {
        return type.equals("employee");
    }

    public boolean isProjectLeader() {
        return type.equals("projectLeader");
    }

    public boolean isSuperuser() {
        return type.equals("superuser");
    }

    public boolean isEmployer() {
        return type.equals("employer");
    }

    public void setEmployee() {
        type = "employee";
    }

    public void setProjectLeader() {
        type = "projectLeader";
    }

    public void setSuperuser() {
        type = "superuser";
    }

    public void setEmployer() {
        type = "employer";
    }

    public byte[] getHashedPassword() {
        return hashedPassword;
    }

    public void setPassword(String password) throws NoSuchAlgorithmException {
        AccountManager myAccountManager = new AccountManager();
        this.salt = myAccountManager.generateSalt();
        this.hashedPassword = myAccountManager.hashPassword(password, salt);
    }

    public String getSalt() {
        return salt;
    }
}
