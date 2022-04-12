package com.messcode.transferobjects;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountManager {

    public boolean equals(User user1, User user2) {
        return user1.getEmail().equals(user2.getEmail());
    }

    public String generateSalt() {
        byte[] array = new byte[10];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    public String generatePassword() {
        while (true) {
            String legalCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
            SecureRandom random = new SecureRandom();
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < 10; i++)
            {
                int randomIndex = random.nextInt(legalCharacters.length());
                stringBuilder.append(legalCharacters.charAt(randomIndex));
            }

            String password = stringBuilder.toString();

            if (passwordRegex(password)) {
                return password;
            }
        }
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

    public boolean passwordRegex(String password) {
        String myRegex = "^(?=.*[a-z].*)(?=.*[A-Z].*)(?=.*[0-9].*)[a-zA-Z0-9~`!@#$%^&*()_\\-+={[}\\]\\|\\:;\"'<,>.?/]]{8,16}$";

        Pattern myPattern = Pattern.compile(myRegex);
        Matcher myMatcher = myPattern.matcher(password);

        return myMatcher.find();
    }

    public boolean emailRegex(String email) {
        String myRegex = "^(?!\\.|-|_)(?!.*\\.\\.|.*--|.*__)(?!.*\\.@|.*-@|.*_@)[a-zA-Z0-9.\\-_]{1,64}@(?!.{255,})((?!-)(?!.*-\\.)[a-zA-Z0-9-]{1,63}\\.)+[a-z]{2,}$";

        Pattern myPattern = Pattern.compile(myRegex);
        Matcher myMatcher = myPattern.matcher(email);

        return myMatcher.find();
    }
}
