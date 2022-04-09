package com.messcode.client.views.private_chat;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.messages.PrivateMessage;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.messcode.transferobjects.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

public class PrivateChatViewModel {
    private MainModel mainModel;

    private ObservableList<User> users;
    private PropertyChangeSupport support;
    private StringProperty message;

    public PrivateChatViewModel(MainModel mainModel) {
        message = new SimpleStringProperty();
        support = new PropertyChangeSupport(this);
        this.mainModel = mainModel;
        users = FXCollections.observableArrayList();
        support = new PropertyChangeSupport(this);
        mainModel.addListener("UsersOnlineInPM", this::addToUserList);
        mainModel.addListener("PrivateMessages", this::displayMessageInChat);
    }

    private void displayMessageInChat(PropertyChangeEvent propertyChangeEvent) {
        PrivateMessage pm = (PrivateMessage) propertyChangeEvent.getNewValue();
        message.setValue(pm.getUsername() + ": " + pm.getMsg());
    }

    private void addToUserList(PropertyChangeEvent propertyChangeEvent) {
        PrivateMessage message = (PrivateMessage) propertyChangeEvent.getNewValue();
        Platform.runLater(() -> {
            users.add(message.getSender());
            users.add(message.getReceiver());
        });
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public void sendMessageInPM(PrivateMessage message) {
        mainModel.sendMessageInPmToServer(message);
    }

    public StringProperty messageProperty() {
        return message;
    }

}
