package com.messcode.transferobjects;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class AccountManager {
    public boolean equals(Employee employee1, Employee employee2) {
        return employee1.id == employee2.id;
    }

    public String generateSalt() {
        byte[] array = new byte[10];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    public byte[] hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        String appendedPassword = password + salt;
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        return digest.digest(appendedPassword.getBytes(StandardCharsets.UTF_8));
    }

    public boolean passwordCheck(Employee myEmployee, String password) throws NoSuchAlgorithmException {
        return Arrays.equals(myEmployee.getHashedPassword(), hashPassword(password, myEmployee.getSalt()));
    }
}
