package com.messcode.client.views.new_group;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;

public class NewGroupViewModel {

    private MainModel mainModel;
    private ObservableList<User> usersList;

    public NewGroupViewModel(MainModel mainModel) {
        this.usersList = FXCollections.observableArrayList();
        this.mainModel = mainModel;
        //  this.usersList.addAll(mainModel.getAllUsers());
        mainModel.addListener("AddOfflineUsers", this::addOfflineUsers);
    }

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

    public ObservableList<User> getUsersList() {
        return usersList;
    }

    public void newGroup(Group g) {
        mainModel.newGroup(g);
    }
}
