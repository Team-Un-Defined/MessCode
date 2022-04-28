package com.messcode.client.views.edit_project_leader;

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
 * The ViewModel of the EditProjectLeader panel.
 * Filters & processes the information going to and from the Controller.
 * @author Kamilla Kisová
 */
public class EditProjectLeaderViewModel implements Subject {
    private Group selectedGroup;
    private ObservableList<User> usersList;
    private PropertyChangeSupport support;
    private MainModel mainModel;

    /**
     * Constructor of the EditProjectLeaderViewModel
     * @param mainModel the MainModel, which manages all the information in the background
     */
    public EditProjectLeaderViewModel(MainModel mainModel) {
        support = new PropertyChangeSupport(this);
        usersList = FXCollections.observableArrayList();
        this.mainModel = mainModel;

        mainModel.addListener("changeSelectedGroup", this::updateGroup);
        mainModel.addListener("ReloadUsers", this::addOfflineUsers);
    }

    /**
     * Adds all the users to the list
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
     * Gets the update from the MainModel
     * @param propertyChangeEvent PropertyChangeEvent triggered event
     */
    private void updateGroup(PropertyChangeEvent propertyChangeEvent) {
        selectedGroup = (Group) propertyChangeEvent.getNewValue();
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

    /**
     * Getter for the list of users
     * @return ObservableList<User>
     */
    public ObservableList<User> getUsers() {
        ObservableList<User> use = FXCollections.observableArrayList();
        use.addAll(usersList);
        use.removeIf(i -> i.getEmail().equals(selectedGroup.getLeader().getEmail()));
        return use;
    }

    /**
     * Getter for the leader of the group
     * @return User
     */
    public User getLeader() {
        return selectedGroup.getLeader();
    }

    /**
     * Initiates the leader change
     * @param u User
     */
    void changeLeader(User u) {
        if (u.getEmail().equals(selectedGroup.getLeader().getEmail())) {
            return;
        }
        Group g = new Group(selectedGroup.getName(), selectedGroup.getDescription(), u);
        mainModel.changeLeader(g);
    }
}
