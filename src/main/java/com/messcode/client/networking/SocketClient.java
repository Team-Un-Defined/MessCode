package com.messcode.client.networking;

import com.messcode.transferobjects.Container;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import com.messcode.transferobjects.User;

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
    public void sendPublic(PublicMessage message) {
        socketHandler.sendPublic(message);
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
}




