package com.messcode.client.model;

import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.GroupMessages;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.util.Subject;
import com.messcode.transferobjects.messages.PublicMessage;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

/**
 *
 */
public interface MainModel extends Subject {

    /**
     * @param mess
     */
    void sendPublic(PublicMessage mess);

    /**
     * @param email
     * @param pwd
     */
    void addUser(String email, String pwd);

    /**
     * @param PMmessage
     */
    void sendListOfPmRoomUsers(PrivateMessage PMmessage);

    /**
     * @param message
     */
    void sendPM(PrivateMessage message);

    /**
     * @param mess
     */
    void sendGroup(GroupMessages mess);

    /**
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @param type
     */
    public void register(String firstName, String lastName, String email, String password,String type);

    /**
     * @param receiver
     * @return
     */
    public ArrayList<PrivateMessage> loadPMs(User receiver);

    /**
     * @return
     */
    public ArrayList<PublicMessage> loadPublics();

    /**
     * @param g
     */
   public void newGroup(Group g);

    /**
     * @param selectedGroup
     * @return
     */
    public ArrayList<GroupMessages> loadGroup(Group selectedGroup);

    /**
     * @param current
     * @param password
     * @param passwordConfirmed
     */
    void changePassword(String current,String password, String passwordConfirmed);

    /**
     * @param selectedGroup
     */
    public void setSelectedGroup(Group selectedGroup);

    /**
     * @return
     */
    User getCurrentUser();

    /**
     * @param use
     */
    void deleteUser(User use);

    /**
     * @param u
     */
    void addMember(ArrayList<User> u);

    /**
     * @param u
     */
    void removeMember(ArrayList<User> u);

    /**
     * @param g
     */
    void deleteGroup(Group g);

    /**
     * @param use
     */
    void resetPassword(User use);

    /**
     * @param g
     */
    void changeLeader(Group g);

    /**
     * @param u
     * @return
     */
    boolean unredPMs(User u);

    /**
     * @param u
     */
    void setSelectedUser(User u);

    /**
     * @return
     */
    User getSelectedUser();

    /**
     * @param g
     * @return
     */
    boolean unredGMs(Group g);
}
