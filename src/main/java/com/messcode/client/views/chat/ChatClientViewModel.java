package com.messcode.client.views.chat;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.messages.PublicMessage;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.messcode.transferobjects.InviteAccept;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.util.Subject;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ChatClientViewModel implements Subject {
    private User currentUser;
    private PropertyChangeSupport support;
    private ObservableList<User> usersList;
    private MainModel mainModel;
    private StringProperty message;

    public ChatClientViewModel(MainModel mainModel) {
        support = new PropertyChangeSupport(this);
        message = new SimpleStringProperty();
        usersList = FXCollections.observableArrayList();
        this.mainModel = mainModel;
        mainModel.addListener("PrivateMessages", this::displayMessageInChat);
        mainModel.addListener("AddNewUser", this::getUsersList);
        mainModel.addListener("MessageForEveryone", this::displayPublic);
        mainModel.addListener("NewPM", this::receivePM);
        mainModel.addListener("SetUsernameInChat", this::setUsernameInChat);
        mainModel.addListener("RemoveUser", this::removeFromUsersList);
    }

    private void removeFromUsersList(PropertyChangeEvent propertyChangeEvent) {
        User user = (User) propertyChangeEvent.getNewValue();
        Platform.runLater(() -> {
            usersList.remove(user);
            System.out.println(usersList);
        });
    }

    private void setUsernameInChat(PropertyChangeEvent propertyChangeEvent) {
        currentUser = (User) propertyChangeEvent.getNewValue();
    }

    private void receivePM(PropertyChangeEvent propertyChangeEvent) {
        PrivateMessage usersPM = ((PrivateMessage) propertyChangeEvent.getNewValue());
        support.firePropertyChange("SendInvite", null, usersPM);
    }

    private void displayPublic(PropertyChangeEvent propertyChangeEvent) {
        PublicMessage publicMessage = (PublicMessage) propertyChangeEvent.getNewValue();

        message.setValue(publicMessage.getUsername() + ": " + publicMessage.getMsg());
        System.out.println("got to model :" + message.getValue());
    }

    private void getUsersList(PropertyChangeEvent propertyChangeEvent) {
        User user = (User) propertyChangeEvent.getNewValue();
        Platform.runLater(() -> {
            usersList.add(user);
            System.out.println(usersList);
        });
    }

    public void sendPublic(PublicMessage mess) {
        mainModel.sendPublic(mess);
    }

    public ObservableList<User> getUsersList() {
        return usersList;
    }

    public StringProperty messageProperty() {
        return message;
    }

    public void sendPM(PrivateMessage mess) {
        mainModel.sendPM(mess);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void sendListOfPmRoomUsers(PrivateMessage usersPM) {
        mainModel.sendListOfPmRoomUsers(usersPM);
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
    

    private void displayMessageInChat(PropertyChangeEvent propertyChangeEvent) {
        PrivateMessage pm = (PrivateMessage) propertyChangeEvent.getNewValue();
        message.setValue(pm.getUsername() + ": " + pm.getMsg());
    }
    
}
