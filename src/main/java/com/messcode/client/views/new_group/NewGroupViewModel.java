package com.messcode.client.views.new_group;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.Group;
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
public class NewGroupViewModel implements Subject {

    private MainModel mainModel;
    private PropertyChangeSupport support;
    private ObservableList<User> usersList;

    /**
     * @param mainModel
     */
    public NewGroupViewModel(MainModel mainModel) {
        this.usersList = FXCollections.observableArrayList();
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
        //  this.usersList.addAll(mainModel.getAllUsers());
        mainModel.addListener("AddOfflineUsers", this::addOfflineUsers);
    }

    /**
     * @param propertyChangeEvent
     */
    private void addOfflineUsers(PropertyChangeEvent propertyChangeEvent) {
        ArrayList<User> users = (ArrayList<User>) propertyChangeEvent.getNewValue();

        Platform.runLater(() -> {
            for (User u : users) {
                if (u.getType().equals("superuser") || u.getType().equals("employer")) {
                    continue;
                }
                usersList.add(u);
            }
            System.out.println(usersList);
        });
    }

    /**
     * @return
     */
    public ObservableList<User> getUsersList() {
        return usersList;
    }

    /**
     * @param g
     */
    public void newGroup(Group g) {
        mainModel.newGroup(g);
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
