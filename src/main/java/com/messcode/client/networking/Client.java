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
 *
 */
public interface Client extends Subject {

    /**
     * @throws IOException
     */
    void start() throws IOException;

    /**
     * @param message
     */
    void displayMessage(PublicMessage message);

    /**
     * @param username
     */
    void addUser(User username);

    /**
     * @param um
     */
    void sendPublic(PublicMessage um);

    /**
     * @param pm
     */
    void sendPM(PrivateMessage pm);

    /**
     * @param newUser
     */
    void register(User newUser);

    /**
     * @param g
     */
    void newGroup(Group g);

    /**
     * @param g
     */
    void refreshGroupList(ArrayList<Group> g);

    /**
     * @param mess
     */
    void sendGroup(GroupMessages mess);

    /**
     * @param u
     */
    void changePassword(User u);

    /**
     * @param use
     */
    void deleteUser(User use);

    /**
     * @param selectedGroup
     */
    void addMember(Group selectedGroup);

    /**
     * @param selectedGroup
     */
    void removeMember(Group selectedGroup);

    /**
     * @param g
     */
    void deleteGroup(Group g);

    /**
     * @param g
     */
    void changeLeader(Group g);

    /**
     * @param use
     */
    void resetPassword(User use);

    void saveDataOnExit(User user);
}
