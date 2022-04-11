package com.messcode.transferobjects;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class AccountManager {
    public boolean equals(User user1, User user2) {
        return user1.getEmail().equals(user2.getEmail());
    }

    public String generateSalt() {
        byte[] array = new byte[10];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    public byte[] hashPassword(String password, String salt) {
        String appendedPassword = password + salt;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            return digest.digest(appendedPassword.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Hashing algorithm does not exist!");
            return null;
        }
    }

    public boolean passwordCheck(User myUser, String password) {
        return Arrays.equals(myUser.getHashedPassword(), hashPassword(password, myUser.getSalt()));
    }

    public User login(String email, String password, UserList userList) {
        for (int i = 0; i < userList.getSize(); i++) {
            User thisUser = userList.get(i);

            if (thisUser.getEmail().equals(email)) {
                if (passwordCheck(thisUser, password)) {
                    return thisUser;
                } else {
                    return null;
                }
            }
        }

        return null;
    }

    public void register(String name, String surname, String email, String password, UserList userList) {
        User newUser = new User(name, surname, email, password);
        userList.addUser(newUser);
    }
}
