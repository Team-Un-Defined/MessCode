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
 * This class is responsible for connecting the client to the server
 *
 */
public class SocketClient implements Client {

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 9090;

    private ClientSocketHandler socketHandler;
    private Socket socket;
    private PropertyChangeSupport support;

    /**
     * Start method initializes Socket, ClientSocketHandler, new Thread, and PropertyChangeSupport
     *
     * @throws IOException connection error
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
     * This method calls the sendPublic method on the sockethandler
     *
     * @param message PublicMessage object
     */
    @Override
    public void sendPublic(PublicMessage message) {
        socketHandler.sendPublic(message);
    }

    /**
     *  This method fires the displayMessage property for the MainModelManager
     *
     * @param message PublicMessage object
     */
    @Override
    public void displayMessage(PublicMessage message) {
        support.firePropertyChange("MessageForEveryone", null, message);
    }

    /**
     *  This method calls the addUser method on the sockethandler
     *
     * @param username User object
     */
    @Override
    public void addUser(User username) {

        socketHandler.addUser(username);
    }

    /**
     *  This method fires the addTolistproperty for the MainModelManager
     *
     * @param user User object
     */
    public void addToList(User user) {
        support.firePropertyChange("AddNewUser", null, user);
    }

    /**
     *  This method calls the addListener method on the PropertyChangeSupport
     *
     * @param eventName String object
     * @param listener PropertyChangeListener object
     */
    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    /**
     *  This method calls the removeListener method on the PropertyChangeSupport
     *
     * @param eventName String object
     * @param listener PropertyChangeListener object
     */
    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    /**
     *  This method fires the displayPM property for the MainModelManager
     *
     * @param pm PrivateMessage object
     */
    public void displayPM(PrivateMessage pm) {
        support.firePropertyChange("newPM", null, pm);
    }

    /**
     *  This method fires the displayGroup property for the MainModelManager
     *
     * @param gm GroupMessage object
     */
    void displayGroup(GroupMessages gm) {
        support.firePropertyChange("newGroupMessage", null, gm);
    }

    /**
     * This method fires the removeFromList property for the MainModelManager
     *
     * @param user User object
     */
    public void removeFromList(User user) {
        support.firePropertyChange("RemoveUser", null, user);
    }

    /**
     * This method calls the sendPM method on the sockethandler
     *
     * @param pm PrivateMessage object
     */
    @Override
    public void sendPM(PrivateMessage pm) {
        socketHandler.sendPM(pm);
    }

    /**
     * This method fires the loginResponse property for the MainModelManager
     *
     * @param answ boolean object
     */
    public void loginResponse(boolean answ) {
        support.firePropertyChange("LoginResponse", null, answ);
    }

    /**
     * This method fires the loginDate property for the MainModelManager
     *
     * @param packet Container object
     */
    public void loginData(Container packet) {
        support.firePropertyChange("LoginData", null, packet);
    }

    /**
     * This method calls the register method on the sockethandler
     *
     * @param newUser User object
     */
    @Override
    public void register(User newUser) {
        socketHandler.register(newUser);
    }

    /**
     * This method fires the refreshGroupList property for the MainModelManager
     *
     * @param g ArrayList<Group></Group> object
     */
    @Override
    public void refreshGroupList(ArrayList<Group> g) {
        support.firePropertyChange("RefresgGroups", null, g);
    }

    /**
     * This method calls the newGroup method on the sockethandler
     *
     * @param g Group object
     */
    @Override
    public void newGroup(Group g) {
        socketHandler.addGroup(g);
    }

    /**
     * This method calls the sendGroup method on the sockethandler
     *
     * @param mess GroupMessages object
     */
    @Override
    public void sendGroup(GroupMessages mess) {
        socketHandler.sendGroup(mess);
    }

    /**
     * This method calls the changePassword method on the sockethandler
     *
     * @param u User object
     */
    @Override
    public void changePassword(User u) {
        socketHandler.changePassword(u);
    }

    /**
     * This method calls the deleteUser method on the sockethandler
     *
     * @param use User object
     */
    @Override
    public void deleteUser(User use) {
        socketHandler.deleteUser(use);
    }

    /**
     * This method fires the userCreateResponse property for the MainModelManager
     *
     * @param acc Container object
     */
    public void userCreateResponse(Container acc) {
        support.firePropertyChange("createUserResponse", null, acc);
    }

    /**
     * This method fires the passChangeResponse property for the MainModelManager
     *
     * @param packet Container object
     */
    public void passChangeResponse(Container packet) {
        support.firePropertyChange("passChangeResponse", null, packet);
    }

    /**
     * This method fires the removeUser property for the MainModelManager
     *
     * @param packet Container object
     */
    public void removeUser(Container packet) {
        support.firePropertyChange("userDeleted", null, packet);
    }

    /**
     * This method calls the addMember method on the sockethandler
     *
     * @param selectedGroup Group object
     */
    @Override
    public void addMember(Group selectedGroup) {
        socketHandler.addMember(selectedGroup);
    }

    /**
     * This method calls the removeMember method on the sockethandler
     *
     * @param selectedGroup Group object
     */
    @Override
    public void removeMember(Group selectedGroup) {
        socketHandler.removeMember(selectedGroup);
    }

    /**
     * This method calls the deleteGroup method on the sockethandler
     *
     * @param g Group object
     */
    @Override
    public void deleteGroup(Group g) {
        socketHandler.deleteGroup(g);
    }

    /**
     * This method calls the resetPassword method on the sockethandler
     *
     * @param use User object
     */
    @Override
    public void resetPassword(User use) {
        socketHandler.resetPassword(use);
    }

    /**
     * This method calls the saveDataOnExit method on the sockethandler
     *
     * @param user User object
     */
    @Override
    public void saveDataOnExit(User user) {
        socketHandler.saveDataOnExit(user);
    }

    /**
     * This method fires the getAllGroupMessages property for the MainModelManager
     *
     * @param pckt Container object
     */
    public void getAllGroupMessages(Container pckt) {
        support.firePropertyChange("AddAllGroupMessages", null, pckt);
    }

    /**
     * This method calls the changeLeader method on the sockethandler
     *
     * @param g Group object
     */
    @Override
    public void changeLeader(Group g) {
        socketHandler.changeLeader(g);
    }

    /**
     * This method fires the addOfflineUser property for the MainModelManager
     *
     * @param packet Container object
     */
    public void addOfflineUser(Container packet) {
        support.firePropertyChange("addOfflineUser", null, packet);
    }

    /**
     * This method fires the kickUser property for the MainModelManager
     *
     * @param packet Container object
     */
    public void kickUser(Container packet) {
        support.firePropertyChange("kickUser",null,packet);
    }
}




