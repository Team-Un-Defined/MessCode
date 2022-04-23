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

public class EditMemberViewModel implements Subject {

    private PropertyChangeSupport support; // idk if needed?
    private MainModel mainModel;
    private ObservableList<User> users ;
    private Group selectedGroup;
    private ObservableList<User> allUsers;


    public EditMemberViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
        allUsers= FXCollections.observableArrayList();
        users= FXCollections.observableArrayList();
        mainModel.addListener("changeSelectedGroup",this::updateUsers);
        mainModel.addListener("AddOfflineUsers", this::addOfflineUsers);
    }

    private void addOfflineUsers(PropertyChangeEvent propertyChangeEvent) {
        ArrayList<User> use = (ArrayList<User>) propertyChangeEvent.getNewValue();

        Platform.runLater(() -> {
            System.out.println("TÖFÖFÖGK");
            allUsers.addAll(use);
            System.out.println(allUsers);
        });
    }
    private void updateUsers(PropertyChangeEvent propertyChangeEvent) {
         selectedGroup = (Group)propertyChangeEvent.getNewValue();
        ObservableList<User> newusers=FXCollections.observableArrayList();
        users.clear();
        newusers.addAll(    selectedGroup .getMembers());
        users.addAll(newusers.filtered(i->!(i.getEmail().equals(   selectedGroup.getLeader().getEmail()))));

    }


    public ObservableList<User> getMembers() {
       return users;
    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    public ObservableList<User> getUsers() {
        return allUsers;
    }
}
