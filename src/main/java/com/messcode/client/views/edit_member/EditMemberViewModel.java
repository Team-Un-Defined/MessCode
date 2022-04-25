package com.messcode.client.views.edit_member;

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
public class EditMemberViewModel implements Subject {

    private PropertyChangeSupport support; // idk if needed?
    private MainModel mainModel;
    private ObservableList<User> users;
    private Group selectedGroup;
    private ObservableList<User> allUsers;
    private ObservableList<User> usersNotInGroup;

    /**
     * @param mainModel
     */
    public EditMemberViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
        allUsers = FXCollections.observableArrayList();
        usersNotInGroup = FXCollections.observableArrayList();
        users = FXCollections.observableArrayList();
        mainModel.addListener("changeSelectedGroup", this::updateUsers);
        mainModel.addListener("AddOfflineUsers", this::addOfflineUsers);
    }

    /**
     * @return
     */
    public Group getSelectedGroup() {
        return selectedGroup;
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
                allUsers.add(u);
            }
            System.out.println(allUsers);
        });
    }

    /**
     * @param propertyChangeEvent
     */
    private void updateUsers(PropertyChangeEvent propertyChangeEvent) {
        selectedGroup = (Group) propertyChangeEvent.getNewValue();
        Platform.runLater(() -> {
            ObservableList<User> newusers = FXCollections.observableArrayList();
            users.clear();
            if (selectedGroup.getLeader() == null) {
                return;
            }
            newusers.addAll(selectedGroup.getMembers());
            users.addAll(newusers.filtered(i -> !(i.getEmail().equals(selectedGroup.getLeader().getEmail()))));
            setUsers();
        });
    }

    /**
     * @return
     */
    public ObservableList<User> getMembers() {
        return users;
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
     *
     */
    public void setUsers() {
        usersNotInGroup.clear();
        ObservableList<User> help = FXCollections.observableArrayList();
        help.addAll(allUsers);
        for (User u : allUsers) {
            for (User c : selectedGroup.getMembers()) {

                if (u.getEmail().equals(c.getEmail()))
                    help.remove(u);
            }
        }
        usersNotInGroup.addAll(help);
    }

    /**
     * @return
     */
    public ObservableList<User> getUsers() {
        return usersNotInGroup;
    }

    /**
     * @param u
     */
    public void addMember(ObservableList<User> u) {
        ArrayList<User> usi = new ArrayList<>();
        usi.addAll(u);
        mainModel.addMember(usi);
    }

    /**
     * @param u
     */
    public void removeMember(ObservableList<User> u) {
        ArrayList<User> usi = new ArrayList<>();
        usi.addAll(u);
        mainModel.removeMember(usi);
    }
}
