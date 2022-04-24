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

public class SocketClient implements Client {

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 9090;

    private ClientSocketHandler socketHandler;
    private Socket socket;
    private PropertyChangeSupport support;

    @Override
    public void start() throws IOException {
        support = new PropertyChangeSupport(this);
        socket = new Socket(SERVER_IP, SERVER_PORT);

        System.out.println("ip " +socket.getInetAddress());
        socketHandler = new ClientSocketHandler(socket, this);
        Thread thread = new Thread(socketHandler);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void sendPublic(PublicMessage message) {
        socketHandler.sendPublic(message);
    }

    @Override
    public void displayMessage(PublicMessage message) {
        support.firePropertyChange("MessageForEveryone", null, message);
    }

    @Override
    public void addUser(User username) {
        System.out.println("IS THIS NULL? "+ username);
        socketHandler.addUser(username);
    }
    
    public void addToList(User user) {
        support.firePropertyChange("AddNewUser", null, user);
    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    public void displayPM(PrivateMessage pm) {
        support.firePropertyChange("newPM", null, pm);
    }
    
     void displayGroup(GroupMessages gm) {
        support.firePropertyChange("newGroupMessage", null, gm);
    }

    public void removeFromList(User user) {
        support.firePropertyChange("RemoveUser", null, user);
    }

    @Override
    public void sendPM(PrivateMessage pm) {
        socketHandler.sendPM(pm);
    }

    public void loginResponse(boolean answ) {
        support.firePropertyChange("LoginResponse", null, answ);
    }

    public void loginData(Container packet) {
        support.firePropertyChange("LoginData", null, packet);
    }

    @Override
    public void register(User newUser) {
        socketHandler.register(newUser);
    }

    @Override
    public void refreshGroupList(ArrayList<Group> g) {
        support.firePropertyChange("RefresgGroups", null, g);
    }
   @Override
    public void newGroup(Group g) {
        socketHandler.addGroup(g);
    }

    @Override
    public void sendGroup(GroupMessages mess) {
        socketHandler.sendGroup(mess);
    }

    @Override
    public void changePassword(User u) {
        socketHandler.changePassword(u);
    }

    @Override
    public void deleteUser(User use) {
        socketHandler.deleteUser(use);
    }


    public void userCreateResponse(Container acc) {

        support.firePropertyChange("createUserResponse", null, acc);
    }

    public void passChangeResponse(Container packet) {
        support.firePropertyChange("passChangeResponse", null, packet);
    }

    public void removeUser(Container packet) {
        support.firePropertyChange("userDeleted", null, packet);
    }

    @Override
    public void addMember(Group selectedGroup) {
       socketHandler.addMember(selectedGroup);
    }

    @Override
    public void removeMember(Group selectedGroup) {
         socketHandler.removeMember(selectedGroup);
    }

    @Override
    public void deleteGroup(Group g) {
        socketHandler.deleteGroup(g);
    }

    @Override
    public void resetPassword(User use) {
        socketHandler.resetPassword(use);
    }

    public void getAllGroupMessages(Container pckt) {
        support.firePropertyChange("AddAllGroupMessages",null,pckt);
    }

    @Override
    public void changeLeader(Group g) {
        socketHandler.changeLeader(g);
    }
}




