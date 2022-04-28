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
 * The ViewModel of the RemoveUser panel.
 * Filters & processes the information going to and from the Controller.
 * @author Kamilla Kisová
 */
public class RemoveUserViewModel implements Subject {
    private MainModel mainModel;
    private PropertyChangeSupport support;

    private ObservableList<User> usersList;

    /**
     * Constructor of the RemoveUserViewModel
     * @param mainModel the MainModel, which manages all the information in the background
     */
    public RemoveUserViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
        usersList= FXCollections.observableArrayList();

        mainModel.addListener("ReloadUsers", this::addOfflineUsers);
    }

    /**
     * Adds all the users to a list
     * @param propertyChangeEvent PropertyChangeEvent triggered event
     */
    private void addOfflineUsers(PropertyChangeEvent propertyChangeEvent) {
       
        ArrayList<User> use = (ArrayList<User>) propertyChangeEvent.getNewValue();
        Platform.runLater(() ->
        {   usersList.clear();
            usersList.addAll(use);
            System.out.println(usersList);
        });
    }

    /**
     * Getter for the list of users
     * @return ObservableList<User>
     */
    public ObservableList<User> getUsers() {
        return usersList.filtered(p-> !p.getSalt().equals(" - deleted"));
    }

    /**
     * Getter for the current user
     * @return User
     */
    public User getCurrentUser()
    {
        return mainModel.getCurrentUser();
    }

    /**
     * Initiates the remove of the user
     * @param use User
     */
    public void deleteUser(User use) {
        mainModel.deleteUser(use);
    }

    /**
     * Method for adding a listener. Inherited from Subject
     * @param eventName String name of the event
     * @param listener PropertyChangeListener listener of the event
     */
    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    /**
     * Method for removing a listener. Inherited from Subject
     * @param eventName String name of the event
     * @param listener PropertyChangeListener listener of the event
     */
    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }
}
