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
 * MainModel interface
 */
public interface MainModel extends Subject {

    /**
     * This method is responsible for calling the sendPublic method on the MainModel
     * @param mess PublicMessage object
     */
    void sendPublic(PublicMessage mess);

    /**
     * This method is responsible for calling the addUser method on the MainModel
     * @param email String object
     * @param pwd String object
     */
    void addUser(String email, String pwd);


    /**
     * This method is responsible for calling the sendPM method on the MainModel
     * @param message PrivateMessage object
     */
    void sendPM(PrivateMessage message);

    /**
     * This method is responsible for calling the sendGroup method on the MainModel
     * @param mess GroupMessage object
     */
    void sendGroup(GroupMessages mess);

    /**
     * This method is responsible for calling the register method on the MainModel
     * @param firstName String object
     * @param lastName String object
     * @param email String object
     * @param password String object
     * @param type String object
     */
    void register(String firstName, String lastName, String email, String password,String type);

    /**
     * This method is responsible for calling the loadPMs method on the MainModel
     * @param receiver User object
     * @return ArrayList<PrivateMessage></PrivateMessage>
     */
    ArrayList<PrivateMessage> loadPMs(User receiver);

    /**
     * This method is responsible for calling the loadPublics method on the MainModel
     * @return ArrayList<PublicMessage></PublicMessage>
     */
    ArrayList<PublicMessage> loadPublics();

    /**
     * This method is responsible for calling the newGroup method on the MainModel
     * @param g Group object
     */
    boolean newGroup(Group g);

    /**
     * This method is responsible for calling the loadGroup method on the MainModel
     * @param selectedGroup Group object
     * @return
     */
    ArrayList<GroupMessages> loadGroup(Group selectedGroup);

    /**
     * This method is responsible for calling the changePassword method on the MainModel
     * @param current String object
     * @param password String object
     * @param passwordConfirmed String object
     */
    void changePassword(String current,String password, String passwordConfirmed);

    /**
     * This method is responsible for calling the setSelectedGroup method on the MainModel
     * @param selectedGroup Group object
     */
    void setSelectedGroup(Group selectedGroup);

    /**
     * This method is responsible for calling the getCurrentUser method on the MainModel
     * @return User object
     */
    User getCurrentUser();

    /**
     * This method is responsible for calling the deleteUser method on the MainModel
     * @param use User object
     */
    void deleteUser(User use);

    /**
     * This method is responsible for calling the addMember method on the MainModel
     * @param u ArrayList<User></User>
     */
    void addMember(ArrayList<User> u);

    /**
     * This method is responsible for calling the removeMember method on the MainModel
     * @param u ArrayList<User></User>
     */
    void removeMember(ArrayList<User> u);

    /**
     * This method is responsible for calling the deleteGroup method on the MainModel
     * @param g Group object
     */
    void deleteGroup(Group g);

    /**
     * This method is responsible for calling the resetPassword method on the MainModel
     * @param use User object
     */
    void resetPassword(User use);

    /**
     * This method is responsible for calling the changeLeader method on the MainModel
     * @param g Group object
     */
    void changeLeader(Group g);

    /**
     * This method is responsible for calling the unredPMs method on the MainModel
     * @param u User object
     * @return boolean
     */
    boolean unredPMs(User u);

    /**
     * This method is responsible for calling the setSelectedUser method on the MainModel
     * @param u User object
     */
    void setSelectedUser(User u);

    /**
     * This method is responsible for calling the getSelectedUser method on the MainModel
     * @return User object
     */
    User getSelectedUser();

    /**
     * This method is responsible for calling the unredGMs method on the MainModel
     * @param g Group object
     * @return boolean
     */
    boolean unredGMs(Group g);

    /**
     * This method is responsible for calling the saveDataOnExit method on the MainModel
     */
    void saveDataOnExit();
}
