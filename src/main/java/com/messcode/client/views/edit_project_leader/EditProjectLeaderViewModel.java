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
 *
 */
public class EditProjectLeaderViewModel implements Subject {
    private Group selectedGroup;
    private ObservableList<User> usersList;
    private PropertyChangeSupport support;
    private MainModel mainModel;

    /**
     * @param mainModel
     */
    public EditProjectLeaderViewModel(MainModel mainModel) {
        support = new PropertyChangeSupport(this);
        usersList = FXCollections.observableArrayList();
        this.mainModel = mainModel;

        mainModel.addListener("changeSelectedGroup", this::updateGroup);
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
     * @param propertyChangeEvent
     */
    private void updateGroup(PropertyChangeEvent propertyChangeEvent) {
        selectedGroup = (Group) propertyChangeEvent.getNewValue();
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

    /**
     * @return
     */
    public ObservableList<User> getUsers() {
        ObservableList<User> use = FXCollections.observableArrayList();
        use.addAll(usersList);
        use.removeIf(i -> i.getEmail().equals(selectedGroup.getLeader().getEmail()));
        return use;
    }

    /**
     * @return
     */
    public User getLeader() {
        return selectedGroup.getLeader();
    }

    /**
     * @param u
     */
    void changeLeader(User u) {
        if (u.getEmail().equals(selectedGroup.getLeader().getEmail())) {
            return;
        }
        Group g = new Group(selectedGroup.getName(), selectedGroup.getDescription(), u);
        mainModel.changeLeader(g);
    }
}
