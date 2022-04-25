package com.messcode.transferobjects;

import com.messcode.transferobjects.messages.PublicMessage;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;

/*
* This class is used for storing important account information such as name, email, RSA keys etc...
* Attribute type can be employee, superuser, projectLeader or employer.
 */
public class User implements Serializable {

    private String type = "employee";
    private String name;
    private String surname;
    private String username;
    private String strPassword;
    private String email;
    private byte[] hashedPassword;
    private String salt;
    private byte[] myPublicKey;
    private byte[] myPrivateKey;
    private ArrayList<PublicMessage> unreadMessages;

    public User(String email, String password) {
        this.email = email;
        this.strPassword=password;
    }


    public User(String salt, String password,String type) {
        AccountManager myAccountManager = new AccountManager();

        this.salt = salt;

        this.hashedPassword = myAccountManager.hashPassword(password, salt);
        System.out.println("JHEÉLLO, USER TALKING pwd: "+password+" salt: "+ salt+ " pwd_hash:" + hashedPassword);

        this.type=type;
    }
    /*
     * If you are creating new employee you use this constructor.
     */
    public User(String name, String surname, String email, String password, String type) {
        AccountManager myAccountManager = new AccountManager();
        MessageEncryptionManager myMessageEncryptionManager = new MessageEncryptionManager();

        KeyPair myKeyPair = myMessageEncryptionManager.generateKeyPair();
        PrivateKey myPrivateKey = myKeyPair.getPrivate();
        PublicKey myPublicKey = myKeyPair.getPublic();

        this.name = name;
        this.surname = surname;
        this.username = name + " " + surname;
        this.email = email;
        this.salt = myAccountManager.generateSalt();

        this.myPublicKey = myMessageEncryptionManager.symmetricDataEncryption(myPublicKey.getEncoded(), password.getBytes());
        this.myPrivateKey = myPrivateKey.getEncoded();

        this.hashedPassword = myAccountManager.hashPassword(password, salt);
        this.unreadMessages = new ArrayList<>();
        this.type=type;
    }

    /*
     * If you took employee from database you use this constructor.
     */
    public User(String name, String surname, String email, byte[] hashedPassword, String salt, String type, byte[] myPrivateKey, byte[] myPublicKey) {
        this.name = name;
        this.surname = surname;
        this.username = name + " " + surname;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.unreadMessages = new ArrayList<>();
        this.type = type;
        this.myPrivateKey = myPrivateKey;
        this.myPublicKey = myPublicKey;
    }

    public byte[] getMyPrivateKey() {
        return myPrivateKey;
    }

    public byte[] getMyPublicKey() {
        return myPublicKey;
    }

    public void setMyPublicKey(byte[] myPublicKey) {
        this.myPublicKey = myPublicKey;
    }

    public void setMyPrivateKey(byte[] myPrivateKey) {
        this.myPrivateKey = myPrivateKey;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void decryptMyPrivateKey(byte[] key) {
        MessageEncryptionManager myMessageEncryptionManager = new MessageEncryptionManager();

        this.myPrivateKey = myMessageEncryptionManager.asymmetricDataDecryption(myPrivateKey, key);
    }

    public String getStrPassword() {
        return strPassword;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }
    public boolean isEmployee() {
        return type.equals("employee");
    }

    public boolean isProjectLeader() {
        return type.equals("project_leader");
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
        type = "project_leader";
    }

    public void setSuperuser() {
        type = "superuser";
    }

    public void setEmployer() {
        type = "employer";
    }

    public String getType() {
        return type;
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
    public void setHashedPassword(byte[] h) {
         this.hashedPassword=h;
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
        return email;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<PublicMessage> getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(ArrayList<PublicMessage> unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public void addUnreadMessages(PublicMessage unreadMessages) {
        this.unreadMessages.add(unreadMessages);
    }

    public void removeUnreadMessages(PublicMessage unreadMessages) {
        this.unreadMessages.remove(unreadMessages);
    }
}
