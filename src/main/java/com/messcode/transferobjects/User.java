package com.messcode.transferobjects;

import com.messcode.transferobjects.messages.GroupMessages;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 *
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
    private ArrayList<PublicMessage> unreadMessages;
    // for login

    /**
     * @param email
     * @param password
     */
    public User(String email, String password) {
        this.email = email;
        this.strPassword = password;
    }

    /**
     * @param salt
     * @param password
     * @param type
     */
    public User(String salt, String password, String type) {
        AccountManager myAccountManager = new AccountManager();

        this.salt = salt;

        this.hashedPassword = myAccountManager.hashPassword(password, salt);
        System.out.println("JHEÉLLO, USER TALKING pwd: " + password + " salt: " + salt + " pwd_hash:" + hashedPassword);

        this.type = type;
    }

    /**
     * @param name
     * @param surname
     * @param email
     * @param password
     * @param type
     */
    // if you are creating new employee you use this constructor
    public User(String name, String surname, String email, String password, String type) {
        AccountManager myAccountManager = new AccountManager();
        this.name = name;
        this.surname = surname;
        this.username = name + " " + surname;
        this.email = email;
        this.salt = myAccountManager.generateSalt();

        this.hashedPassword = myAccountManager.hashPassword(password, salt);
        this.unreadMessages = new ArrayList<>();
        this.type = type;
    }

    /**
     * @param salt
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * @param name
     * @param surname
     * @param email
     * @param hashedPassword
     * @param salt
     * @param type
     */
    // if you took employee from database you use this constructor
    public User(String name, String surname, String email, byte[] hashedPassword, String salt, String type) {
        this.name = name;
        this.surname = surname;
        this.username = name + " " + surname;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.unreadMessages = new ArrayList<>();
        this.type = type;
    }

    /**
     * @return
     */
    public String getStrPassword() {
        return strPassword;
    }

    /**
     * @param strPassword
     */
    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    /**
     * @return
     */
    public boolean isEmployee() {
        return type.equals("employee");
    }

    /**
     * @return
     */
    public boolean isProjectLeader() {
        return type.equals("project_leader");
    }

    /**
     * @return
     */
    public boolean isSuperuser() {
        return type.equals("superuser");
    }

    /**
     * @return
     */
    public boolean isEmployer() {
        return type.equals("employer");
    }

    /**
     *
     */
    public void setEmployee() {
        type = "employee";
    }

    /**
     *
     */
    public void setProjectLeader() {
        type = "project_leader";
    }

    /**
     *
     */
    public void setSuperuser() {
        type = "superuser";
    }

    /**
     *
     */
    public void setEmployer() {
        type = "employer";
    }

    /**
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
        updateUsername();
    }

    /**
     * @return
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
        updateUsername();
    }

    /**
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     */
    public void updateUsername() {
        username = name + " " + surname;
    }

    /**
     * @return
     */
    public byte[] getHashedPassword() {
        return hashedPassword;
    }

    /**
     * @param h
     */
    public void setHashedPassword(byte[] h) {
        this.hashedPassword = h;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        AccountManager myAccountManager = new AccountManager();
        this.salt = myAccountManager.generateSalt();
        this.hashedPassword = myAccountManager.hashPassword(password, salt);
    }

    /**
     * @return
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return email;
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return
     */
    public ArrayList<PublicMessage> getUnreadMessages() {
        return unreadMessages;
    }

    /**
     * @return
     */
    public ArrayList<PrivateMessage> getUnreadPMs() {
        ArrayList<PrivateMessage> pm = new ArrayList<PrivateMessage>();
        for (PublicMessage un : unreadMessages) {
            if (un instanceof PrivateMessage) {
                pm.add((PrivateMessage) un);
            }
        }
        return pm;
    }

    /**
     * @return
     */
    public ArrayList<GroupMessages> getUnreadGMs() {
        ArrayList<GroupMessages> gm = new ArrayList<GroupMessages>();
        for (PublicMessage un: unreadMessages){
            if (un instanceof GroupMessages){
                gm.add((GroupMessages) un);
            }
        }
        return gm;
    }

    /**
     * @param unreadMessages
     */
    public void setUnreadMessages(ArrayList<PublicMessage> unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    /**
     * @param unreadMessages
     */
    public void addUnreadMessages(PublicMessage unreadMessages) {
        this.unreadMessages.add(unreadMessages);
    }

    /**
     * @param unreadMessages
     */
    public void removeUnreadMessages(PublicMessage unreadMessages) {
        this.unreadMessages.remove(unreadMessages);
    }
}
