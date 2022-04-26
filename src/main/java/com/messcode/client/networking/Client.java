package com.messcode.client.networking;

import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.GroupMessages;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import com.messcode.transferobjects.util.Subject;

import java.io.IOException;
import java.util.ArrayList;

/**
 *Client interface
 */
public interface Client extends Subject {

    /**
     * Start method for the client
     *
     * @throws IOException errors
     */
    void start() throws IOException;

    /**
     * This method is responsible for calling the displayMessage method on the SocketClient
     *
     * @param message
     */
    void displayMessage(PublicMessage message);

    /**
     *This method is responsible for calling the addUser method on the SocketClient
     *
     * @param username User object
     */
    void addUser(User username);

    /**
     * This method is responsible for calling the sendPublic method on the SocketClient
     *
     * @param um PublicMessage object
     */
    void sendPublic(PublicMessage um);

    /**
     * This method is responsible for calling the sendPM method on the SocketClient
     *
     * @param pm PrivateMessage object
     */
    void sendPM(PrivateMessage pm);

    /**
     * This method is responsible for calling the register method on the SocketClient
     *
     * @param newUser User object
     */
    void register(User newUser);

    /**
     * This method is responsible for calling the newGroup method on the SocketClient
     *
     * @param g Group object
     */
    void newGroup(Group g);

    /**
     * This method is responsible for calling the refereshGroupList method on the SocketClient
     *
     * @param g Arraylist<Group></Group> object
     */
    void refreshGroupList(ArrayList<Group> g);

    /**
     * This method is responsible for calling the sendGroup method on the SocketClient
     *
     * @param mess GroupMessages object
     */
    void sendGroup(GroupMessages mess);

    /**
     * This method is responsible for calling the changePassword method on the SocketClient
     *
     * @param u User object
     */
    void changePassword(User u);

    /**
     * This method is responsible for calling the deleteUser method on the SocketClient
     *
     * @param use User object
     */
    void deleteUser(User use);

    /**
     * This method is responsible for calling the addMember method on the SocketClient
     *
     * @param selectedGroup Group object
     */
    void addMember(Group selectedGroup);

    /**
     * This method is responsible for calling the removeMember method on the SocketClient
     *
     * @param selectedGroup Group object
     */
    void removeMember(Group selectedGroup);

    /**
     * This method is responsible for calling the deleteGroup method on the SocketClient
     *
     * @param g Group object
     */
    void deleteGroup(Group g);

    /**
     * This method is responsible for calling the changeLeader method on the SocketClient
     *
     * @param g Group object
     */
    void changeLeader(Group g);

    /**
     * This method is responsible for calling the resetPassword method on the SocketClient
     *
     * @param use User object
     */
    void resetPassword(User use);

    /**
     * This method is responsible for calling the saveDataOnExit method on the SocketClient
     *
     * @param user User object
     */
    void saveDataOnExit(User user);
}
