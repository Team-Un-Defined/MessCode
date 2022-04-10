package com.messcode.client.model;

import com.messcode.client.networking.Client;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import com.messcode.transferobjects.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

public class MainModelManager implements MainModel {
    private Client client;
    private PropertyChangeSupport support;
    private User user;
    private PrivateMessage usersPM;

    public MainModelManager(Client client) {
        support = new PropertyChangeSupport(this);
        this.client = client;
        try {
            client.start();
            client.addListener("AddNewUser", this::addToUsersList);
            client.addListener("MessageForEveryone", this::receivePublic);
            client.addListener("newPM", this::receivePM);
            client.addListener("RemoveUser", this::removeFromUsersList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeFromUsersList(PropertyChangeEvent propertyChangeEvent) {
        support.firePropertyChange(propertyChangeEvent);
    }
    
    //  GLOBAL CHAT
    @Override
    public void sendListOfPmRoomUsers(PrivateMessage usersPM) {
        this.usersPM = usersPM;
        support.firePropertyChange("UsersOnlineInPM", null, usersPM);
    }

    @Override
    public void receivePublic(
            PropertyChangeEvent propertyChangeEvent) {
        PublicMessage publicMessage = (PublicMessage) propertyChangeEvent.getNewValue();
        System.out.println("got to model");
        support.firePropertyChange("MessageForEveryone", null, publicMessage);
    }

    @Override
    public void receivePM(
        PropertyChangeEvent propertyChangeEvent) {
        PrivateMessage pm = (PrivateMessage) propertyChangeEvent.getNewValue();
        System.out.println("//////////////////////////PMPM//////////////////////////////");
        support.firePropertyChange("newPM", null, pm);
    }

    @Override
    public void addToUsersList(PropertyChangeEvent propertyChangeEvent) {
        User user = (User) propertyChangeEvent.getNewValue();
        support.firePropertyChange("AddNewUser", null, user);
    }

    @Override
    public void addUser(User username) {
        client.addUser(username);
        this.user = username;
        support.firePropertyChange("SetUsernameInChat", null, username);
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

    @Override
    public void sendPublic(PublicMessage mess) {
        client.sendPublic(mess);
        
    }

    @Override
    public void sendPM(PrivateMessage message) {
        client.sendPM(message);
    }
}
