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
 * The ViewModel of the NewGroup panel.
 * Filters & processes the information going to and from the Controller.
 * @author Kamilla Kisová
 */
public class NewGroupViewModel implements Subject {

    private MainModel mainModel;
    private PropertyChangeSupport support;
    private ObservableList<User> usersList;

    /**
     * Constructor of the NewGroupViewModel
     * @param mainModel the MainModel, which manages all the information in the background
     */
    public NewGroupViewModel(MainModel mainModel) {
        this.usersList = FXCollections.observableArrayList();
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
        //  this.usersList.addAll(mainModel.getAllUsers());
        mainModel.addListener("ReloadUsers", this::addOfflineUsers);
    }

    /**
     * Adds all the users to a list
     * @param propertyChangeEvent PropertyChangeEvent triggered event
     */
    private void addOfflineUsers(PropertyChangeEvent propertyChangeEvent) {
        ArrayList<User> users = (ArrayList<User>) propertyChangeEvent.getNewValue();
        Platform.runLater(() -> {
            for (User u : users) {
                if (u.isSuperuser() || u.isEmployer()) {
                    continue;
                }
                usersList.add(u);
            }
            System.out.println(usersList);
        });
    }

    /**
     * Getter for the usersList
     * @return the list of users usersList
     */
    public ObservableList<User> getUsersList() {
        return usersList.filtered(p-> !p.getSalt().equals(" - deleted"));
    }

    /**
     * Initializes the creation of the new group
     * @param g Group
     */
    public boolean newGroup(Group g) {
        
    return  mainModel.newGroup(g);
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
