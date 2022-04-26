package com.messcode.transferobjects;

import com.messcode.transferobjects.messages.GroupMessages;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * This class is used for storing important account information such as name, email, RSA keys etc...
 * Attribute type can be employee, superuser, projectLeader or employer.
 * @author Martin Šváb
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

    /**
     * @param email email address
     * @param password open password
     */
    public User(String email, String password) {
        this.email = email;
        this.strPassword = password;
    }

    /**
     * This constructor is used when creating new employee.
     *
     * @param name first name
     * @param surname last name
     * @param email email address
     * @param password open password
     * @param type type of user (employee/employer/projectLeader/superuser)
     */
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
     * @param salt string appended to password before hashing it
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * This constructor is used when you get employee information from database
     *
     * @param name first name
     * @param surname last name
     * @param email email address
     * @param hashedPassword hashed password
     * @param salt string appended to password before hashing it
     * @param type type of user (employee/employer/projectLeader/superuser)
     */
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
     * @return string version of password
     */
    public String getStrPassword() {
        return strPassword;
    }

    /**
     * @param strPassword string version of password
     */
    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    /**
     * @return true if type is employee
     */
    public boolean isEmployee() {
        return type.equals("employee");
    }

    /**
     * @return true if type is project leader
     */
    public boolean isProjectLeader() {
        return type.equals("project_leader");
    }

    /**
     * @return true if type is superuser
     */
    public boolean isSuperuser() {
        return type.equals("superuser");
    }

    /**
     * @return true if type is employer
     */
    public boolean isEmployer() {
        return type.equals("employer");
    }

    /**
     * Sets the type to employee.
     */
    public void setEmployee() {
        type = "employee";
    }

    /**
     * Sets the type to project leader.
     */
    public void setProjectLeader() {
        type = "project_leader";
    }

    /**
     * Sets the type to superuser.
     */
    public void setSuperuser() {
        type = "superuser";
    }

    /**
     * Sets the type to employer.
     */
    public void setEmployer() {
        type = "employer";
    }

    /**
     * @return type of user
     */
    public String getType() {
        return type;
    }

    /**
     * @return email of user
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email email of user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return first name of user
     */
    public String getName() {
        return name;
    }

    /**
     * @param name first name of user
     */
    public void setName(String name) {
        this.name = name;
        updateUsername();
    }

    /**
     * @return last name of user
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname last name of user
     */
    public void setSurname(String surname) {
        this.surname = surname;
        updateUsername();
    }

    /**
     * @return full username of user
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
     * @return hashed version of password
     */
    public byte[] getHashedPassword() {
        return hashedPassword;
    }

    /**
     * @param h hashed version of password
     */
    public void setHashedPassword(byte[] h) {
        this.hashedPassword = h;
    }

    /**
     * This method generates salt and hashes the password after appending it with the generated salt.
     *
     * @param password string version of password
     */
    public void setPassword(String password) {
        AccountManager myAccountManager = new AccountManager();
        this.salt = myAccountManager.generateSalt();
        this.hashedPassword = myAccountManager.hashPassword(password, salt);
    }

    /**
     * @return string appended to password before hashing it
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @return email of user
     */
    @Override
    public String toString() {
        return email;
    }

    /**
     * @param type type of user
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return all unread messages
     */
    public ArrayList<PublicMessage> getUnreadMessages() {
        return unreadMessages;
    }

    /**
     * @return all unread private messages
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
     * @return all unread group messages
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
     * @param unreadMessages all unread public messages
     */
    public void setUnreadMessages(ArrayList<PublicMessage> unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    /**
     * @param unreadMessages all unread messages
     */
    public void addUnreadMessages(PublicMessage unreadMessages) {
        if(unreadMessages.getSender()==null)return;
        int end=0;
        ListIterator<PublicMessage> listIterator = this.unreadMessages.listIterator();
       if(unreadMessages instanceof PrivateMessage){
       while(listIterator.hasNext()){
      PublicMessage mess = listIterator.next();
  
       if(mess instanceof PrivateMessage){
           
       String senderNow = unreadMessages.getSender().getEmail();
       String senderThen = mess.getSender().getEmail();
       String receiverNow = ((PrivateMessage) unreadMessages).getReceiver().getEmail();
       String receiverThen = ((PrivateMessage) mess).getReceiver().getEmail();
           
       System.out.println("GOT NOW SENDER: "+senderNow + "GOT THEN SENDER: "+ senderThen + "GOT NOW RECEICER: "+ receiverNow + "GOT THEN RECEIVER: " + receiverThen);
       System.out.println("SENDER NOW = SENDER THEN ?  "+(senderNow.equals(senderThen)&& receiverNow.contains(receiverThen)));
       System.out.println("SENDER Then = SENDER NOW ?  "+ (senderNow.equals(receiverThen) && receiverNow.equals(senderThen)));
       if((senderNow.equals(senderThen)&& receiverNow.contains(receiverThen)) || (senderNow.equals(receiverThen) && receiverNow.equals(senderThen) ))
       {end=1;
           if(mess.getTime().getNanos()< unreadMessages.getTime().getNanos()){
              System.out.println("///////////////////////_____1_________///////////////////////////////////");
              getUnreadPMs().forEach(h-> System.out.println("[RECEIVER] "+h.getReceiver().getEmail() + "[SENDER] "+ h.getSender().getEmail() ));
              System.out.println("//////////////////////////////////////////////////////////");
   
            listIterator.set(unreadMessages);
            
            System.out.println("///////////////////////_____2________///////////////////////////////////");
            getUnreadPMs().forEach(h-> System.out.println("[RECEIVER] "+h.getReceiver().getEmail() + "[SENDER] "+ h.getSender().getEmail() ));
            System.out.println("//////////////////////////////////////////////////////////");
            end=1;
           }
       }
       }
       }
         if(end==1){return;}
       listIterator.add(unreadMessages);
       System.out.println("///////////////////////_____3_____///////////////////////////////////");
       getUnreadPMs().forEach(h-> System.out.println("[RECEIVER] "+h.getReceiver().getEmail() + "[SENDER] "+ h.getSender().getEmail() ));
       System.out.println("//////////////////////////////////////////////////////////");
       return;
       }
       
        
       else this.unreadMessages.add(unreadMessages);
    }

    /**
     * @param unreadMessages all unread messages
     */
    public void removeUnreadMessages(PublicMessage unreadMessages) {
        this.unreadMessages.remove(unreadMessages);
    }
}
