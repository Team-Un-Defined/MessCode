package com.messcode.client.views.remove_user;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.util.Subject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 *
 */
public class RemoveUserViewModel implements Subject {
    private MainModel mainModel;
    private PropertyChangeSupport support;

    private ObservableList<User> usersList;

    /**
     * @param mainModel
     */
    public RemoveUserViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
        usersList= FXCollections.observableArrayList();

        mainModel.addListener("AddOfflineUsers", this::addOfflineUsers);
    }

    /**
     * @param propertyChangeEvent
     */
    private void addOfflineUsers(PropertyChangeEvent propertyChangeEvent) {
        ArrayList<User> use = (ArrayList<User>) propertyChangeEvent.getNewValue();

        Platform.runLater(() -> {
            System.out.println("TÖFÖFÖGK");
            use.removeIf(user -> user.getSalt().equals(" - deleted") );
            usersList.addAll(use);
            System.out.println(usersList);
        });
    }

    /**
     * @return
     */
    public ObservableList<User> getUsers() {
        return usersList;
    }

    /**
     * @return
     */
    public User getCurrentUser()
    {
        return mainModel.getCurrentUser();
    }

    /**
     * @param use
     */
    public void deleteUser(User use) {
        mainModel.deleteUser(use);
        Platform.runLater(() -> {
            usersList.remove(use);
        });
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
}
