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
 * The ViewModel of the EditMember panel.
 * Filters & processes the information going to and from the Controller.
 * @author Kamilla Kisová
 */
public class EditMemberViewModel implements Subject {

    private PropertyChangeSupport support; // idk if needed?
    private MainModel mainModel;
    private ObservableList<User> users;
    private Group selectedGroup;
    private ObservableList<User> allUsers;
    private ObservableList<User> usersNotInGroup;

    /**
     * Constructor of the EditMemberViewModel
     * @param mainModel the MainModel, which manages all the information in the background
     */
    public EditMemberViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
        allUsers = FXCollections.observableArrayList();
        usersNotInGroup = FXCollections.observableArrayList();
        users = FXCollections.observableArrayList();
        mainModel.addListener("changeSelectedGroup", this::updateUsers);
        mainModel.addListener("AddOfflineUsers", this::addOfflineUsers);
    }

    /**
     * Getter for the selected group
     * @return Group
     */
    public Group getSelectedGroup() {
        return selectedGroup;
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
                allUsers.add(u);
            }
            System.out.println(allUsers);
        });
    }

    /**
     * Updates the list of users
     * @param propertyChangeEvent PropertyChangeEvent triggered event
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
     * Getter for the members of the group
     * @return ObservableList<User>
     */
    public ObservableList<User> getMembers() {
        return users;
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
     * Sets the users (not in group) for the list
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
     * Getter for the users that aren't in the group
     * @return ObservableList<User>
     */
    public ObservableList<User> getUsers() {
        return usersNotInGroup;
    }

    /**
     * Initiates the adding of the member(s)
     * @param u ObservableList<User>
     */
    public void addMember(ObservableList<User> u) {
        ArrayList<User> usi = new ArrayList<>();
        usi.addAll(u);
        mainModel.addMember(usi);
    }

    /**
     * Initiates the removal of the member(s)
     * @param u ObservableList<User>
     */
    public void removeMember(ObservableList<User> u) {
        ArrayList<User> usi = new ArrayList<>();
        usi.addAll(u);
        mainModel.removeMember(usi);
    }
}
