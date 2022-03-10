package com.messcode.client.networking;

import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.UsersPM;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.Socket;

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
        socketHandler = new ClientSocketHandler(socket, this);
        Thread thread = new Thread(socketHandler);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void sendMessage(PublicMessage message) {
        socketHandler.sendMessage(message);
    }

    @Override
    public void invitePmToServer(UsersPM usersPM) {
        socketHandler.sendInvitePMtoServer(usersPM);
    }

    @Override
    public void sendMessageInPMToServer(PrivateMessage message) {
        socketHandler.sendMessageInPM(message);

    }

    @Override
    public void displayMessage(PublicMessage message) {
        support.firePropertyChange("MessageForEveryone", null, message);
    }

    @Override
    public void addUser(User username) {
        socketHandler.addUser(username);
    }

    public void addToList(User user) {
        System.out
                .println("[CLIENT] user " + user.getUsername() + " added to list");
        support.firePropertyChange("AddNewUser", null, user);
    }


    @Override
    public void addListener(String eventName,
                            PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName,
                               PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    public void sendInvitePmFromServer(UsersPM usersPM) {
        support.firePropertyChange("SendInvitePM", null, usersPM);
    }

    public void displayMessagesPM(PrivateMessage pm) {
        support.firePropertyChange("PrivateMessages", null, pm);
    }

    public void removeFromList(User user) {
        support.firePropertyChange("RemoveUser", null, user);
    }
}




