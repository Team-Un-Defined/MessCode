package com.messcode.client.networking;

import com.messcode.transferobjects.Container;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.GroupMessages;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 */
public class SocketClient implements Client {

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 9090;

    private ClientSocketHandler socketHandler;
    private Socket socket;
    private PropertyChangeSupport support;

    /**
     * @throws IOException
     */
    @Override
    public void start() throws IOException {
        support = new PropertyChangeSupport(this);
        socket = new Socket(SERVER_IP, SERVER_PORT);

        System.out.println("ip " + socket.getInetAddress());
        socketHandler = new ClientSocketHandler(socket, this);
        Thread thread = new Thread(socketHandler);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * @param message
     */
    @Override
    public void sendPublic(PublicMessage message) {
        socketHandler.sendPublic(message);
    }

    /**
     * @param message
     */
    @Override
    public void displayMessage(PublicMessage message) {
        support.firePropertyChange("MessageForEveryone", null, message);
    }

    /**
     * @param username
     */
    @Override
    public void addUser(User username) {
        System.out.println("IS THIS NULL? " + username);
        socketHandler.addUser(username);
    }

    /**
     * @param user
     */
    public void addToList(User user) {
        support.firePropertyChange("AddNewUser", null, user);
    }

    /**
     * @param eventName
     * @param listener
     */
    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    /**
     * @param eventName
     * @param listener
     */
    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    /**
     * @param pm
     */
    public void displayPM(PrivateMessage pm) {
        support.firePropertyChange("newPM", null, pm);
    }

    /**
     * @param gm
     */
    void displayGroup(GroupMessages gm) {
        support.firePropertyChange("newGroupMessage", null, gm);
    }

    /**
     * @param user
     */
    public void removeFromList(User user) {
        support.firePropertyChange("RemoveUser", null, user);
    }

    /**
     * @param pm
     */
    @Override
    public void sendPM(PrivateMessage pm) {
        socketHandler.sendPM(pm);
    }

    /**
     * @param answ
     */
    public void loginResponse(boolean answ) {
        support.firePropertyChange("LoginResponse", null, answ);
    }

    /**
     * @param packet
     */
    public void loginData(Container packet) {
        support.firePropertyChange("LoginData", null, packet);
    }

    /**
     * @param newUser
     */
    @Override
    public void register(User newUser) {
        socketHandler.register(newUser);
    }

    /**
     * @param g
     */
    @Override
    public void refreshGroupList(ArrayList<Group> g) {
        support.firePropertyChange("RefresgGroups", null, g);
    }

    /**
     * @param g
     */
    @Override
    public void newGroup(Group g) {
        socketHandler.addGroup(g);
    }

    /**
     * @param mess
     */
    @Override
    public void sendGroup(GroupMessages mess) {
        socketHandler.sendGroup(mess);
    }

    /**
     * @param u
     */
    @Override
    public void changePassword(User u) {
        socketHandler.changePassword(u);
    }

    /**
     * @param use
     */
    @Override
    public void deleteUser(User use) {
        socketHandler.deleteUser(use);
    }

    /**
     * @param acc
     */
    public void userCreateResponse(Container acc) {
        support.firePropertyChange("createUserResponse", null, acc);
    }

    /**
     * @param packet
     */
    public void passChangeResponse(Container packet) {
        support.firePropertyChange("passChangeResponse", null, packet);
    }

    /**
     * @param packet
     */
    public void removeUser(Container packet) {
        support.firePropertyChange("userDeleted", null, packet);
    }

    /**
     * @param selectedGroup
     */
    @Override
    public void addMember(Group selectedGroup) {
        socketHandler.addMember(selectedGroup);
    }

    /**
     * @param selectedGroup
     */
    @Override
    public void removeMember(Group selectedGroup) {
        socketHandler.removeMember(selectedGroup);
    }

    /**
     * @param g
     */
    @Override
    public void deleteGroup(Group g) {
        socketHandler.deleteGroup(g);
    }

    /**
     * @param use
     */
    @Override
    public void resetPassword(User use) {
        socketHandler.resetPassword(use);
    }

    /**
     * @param user
     */
    @Override
    public void saveDataOnExit(User user) {
        socketHandler.saveDataOnExit(user);
    }

    /**
     * @param pckt
     */
    public void getAllGroupMessages(Container pckt) {
        support.firePropertyChange("AddAllGroupMessages", null, pckt);
    }

    /**
     * @param g
     */
    @Override
    public void changeLeader(Group g) {
        socketHandler.changeLeader(g);
    }

    public void addOfflineUser(Container packet) {
        support.firePropertyChange("addOfflineUser", null, packet);
    }

    public void kickUser(Container packet) {
        support.firePropertyChange("kickUser",null,packet);
    }
}




