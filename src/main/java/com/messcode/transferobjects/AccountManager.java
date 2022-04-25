package com.messcode.transferobjects;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used for generating passwords, salts and password encryption.
 * It also contains some regexes for checking validity of account information.
 */
public class AccountManager {

    /**
     * Checks if 2 user classes are the same using their emails.
     *
     * @param user1
     * @param user2
     * @return true if users are equal false if they are not equal
     */
    public boolean equals(User user1, User user2) {
        return user1.getEmail().equals(user2.getEmail());
    }

    /**
     * Generates a random 10 character long String.
     *
     * @return salt that is used in hashing algorithm
     */
    public String generateSalt() {
        byte[] array = new byte[10];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    /**
     * Generates a random10 character long password.
     * This generated password adheres to the password policy.
     *
     * @return random password
     */
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

    /**
     * This method encrypts password using SHA-512 hashing algorithm.
     *
     * @param password    open text of password
     * @param salt        this string is appended to password before hashing
     * @return hash value of password
     */
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

    /**
     * Checks if group name has valid format.
     * Valid group name format is having minimum of 4 characters.
     *
     * @param groupName this String is checked
     * @return true if group name is valid, false if it is not valid
     */
    public boolean groupNameRegex(String groupName) {
        String myRegex = "^.{4,}$";

        Pattern myPattern = Pattern.compile(myRegex);
        Matcher myMatcher = myPattern.matcher(groupName);

        return myMatcher.find();
    }

    /**
     * Checks if account name has valid format.
     * Valid account name format is only using regular letters and letters using diacritics.
     * The first character has to be uppercase letter, the rest are lowercase letters.
     *
     * @param name   this String is checked
     * @return true if account name is valid, false if it is not valid
     */
    public boolean nameRegex(String name) {
        String myRegex = "^[A-ZŠĽČŤŽŇÝÁÉÚÍÄÔ]{1}[a-zšľčťžňýáíéúäô]{1,}$";

        Pattern myPattern = Pattern.compile(myRegex);
        Matcher myMatcher = myPattern.matcher(name);

        return myMatcher.find();
    }

    /**
     * Checks if password has valid format.
     * Valid group name format is having 8-16 characters.
     * Password must contain 1 lowercase letter, 1 uppercase letter and 1 number.
     * It can contain lowercase letters, uppercase letters, numbers and special symbols.
     *
     * @param password   this String is checked
     * @return true if password is valid, false if it is not valid
     */
    public boolean passwordRegex(String password) {
        String myRegex = "^(?=.*[a-z].*)(?=.*[A-Z].*)(?=.*[0-9].*)[a-zA-Z0-9~`!@#$%^&*()_\\-+={[}\\]\\|\\:;\"'<,>.?/]]{8,16}$";

        Pattern myPattern = Pattern.compile(myRegex);
        Matcher myMatcher = myPattern.matcher(password);

        return myMatcher.find();
    }

    /**
     * Checks if email has valid format.
     * Valid email format is local part followed by @ then followed by domain part
     * Local part of email can contain lowercase letters, uppercase letters and numbers without restrictions.
     * Local part of email can also contain some special characters, but they cannot be the first or last characters and there cannot be 2 special characters in a row.
     * Local part of email must be 1-64 characters long.
     * Domain part of email can contain lowercase letters, uppercase letters and numbers without restrictions.
     * Domain part of email can also contain some special characters, but they cannot be the first or last characters and there cannot be 2 special characters in a row.
     * Domain part of email can contain multiple domains each separated by comma. Each domain can be at most 63 characters long.
     * Top level domain in domain part must be at least 2 characters long.
     *
     * @param email   this String is checked
     * @return true if password is valid, false if it is not valid
     */
    public boolean emailRegex(String email) {
        String myRegex = "^(?!\\.|-|_)(?!.*\\.\\.|.*--|.*__)(?!.*\\.@|.*-@|.*_@)[a-zA-Z0-9.\\-_]{1,64}@(?!.{255,})((?!-)(?!.*-\\.)[a-zA-Z0-9-]{1,63}\\.)+[a-z]{2,}$";

        Pattern myPattern = Pattern.compile(myRegex);
        Matcher myMatcher = myPattern.matcher(email);

        return myMatcher.find();
    }
}
