package com.messcode.client.views.view_group;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.util.Subject;
import java.beans.PropertyChangeEvent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ViewGroupViewModel implements Subject {
    private MainModel mainModel;
    private PropertyChangeSupport support;
    private Group selectedGroup;
    private ObservableList<User> users; 
    private ObservableList<User> allUsers;
       private ObservableList<User> usersNotInGroup;

    public ViewGroupViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
        usersNotInGroup = FXCollections.observableArrayList();
        users = FXCollections.observableArrayList();
        allUsers = FXCollections.observableArrayList();
        support = new PropertyChangeSupport(this);
        mainModel.addListener("changeSelectedGroup", this::updateUsers);
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
    
     public ObservableList<User> getMembers() {
        return users;
    }

    String getGroupName() {
        return selectedGroup.getName();
    }

    String getLead() {
       
        return selectedGroup.getLeader().getName() + "  " + selectedGroup.getLeader().getSurname();
    }

    String getDescription() {
       return selectedGroup.getDescription();
    }
     
     
     
    
}
