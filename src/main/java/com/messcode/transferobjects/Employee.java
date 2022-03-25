package com.messcode.transferobjects;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class Employee extends User {
    String name;
    String surname;
    String email;
    byte[] hashedPassword;
    String salt;

    Employee(String name, String surname, String email, String password) throws NoSuchAlgorithmException {
        super(name + " " + surname);
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.salt = generateSalt();
        this.hashedPassword = hashPassword(password, salt);
    }

    Employee(String name, String surname, String email, byte[] hashedPassword, String salt) {
        super(name + " " + surname);
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
    }

    public byte[] hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        String appendedPassword = password + salt;
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        return digest.digest(appendedPassword.getBytes(StandardCharsets.UTF_8));
    }

    public String generateSalt() {
        byte[] array = new byte[10];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    public boolean passwordCheck(String password) throws NoSuchAlgorithmException {
        return Arrays.equals(this.hashedPassword, hashPassword(password, salt));
    }

}
