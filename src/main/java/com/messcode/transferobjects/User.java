package com.messcode.transferobjects;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

public class
User implements Serializable {
    private String type = "employee";
    private String name;
    private String surname;
    private String username;
    private String email;
    private byte[] hashedPassword;
    private String salt;

    // if you are creating new employee you use this constructor
    public User(String name, String surname, String email, String password) {
        AccountManager myAccountManager = new AccountManager();
        this.name = name;
        this.surname = surname;
        this.username = name + " " + surname;
        this.email = email;
        this.salt = myAccountManager.generateSalt();
        this.hashedPassword = myAccountManager.hashPassword(password, salt);
    }

    // if you took employee from database you use this constructor
    public User(String name, String surname, String email, byte[] hashedPassword, String salt) {
        this.name = name;
        this.surname = surname;
        this.username = name + " " + surname;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateUsername();
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
        updateUsername();
    }

    public String getUsername() {
        return username;
    }

    public void updateUsername() {
        username = name + " " + surname;
    }

    public byte[] getHashedPassword() {
        return hashedPassword;
    }

    public void setPassword(String password) {
        AccountManager myAccountManager = new AccountManager();
        this.salt = myAccountManager.generateSalt();
        this.hashedPassword = myAccountManager.hashPassword(password, salt);
    }

    public String getSalt() {
        return salt;
    }

    @Override
    public String toString() {
        return username;
    }
}
