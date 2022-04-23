package com.messcode.client.views.remove_user;

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

public class RemoveUserViewModel {
    private MainModel mainModel;

    private ObservableList<User> usersList;

    public RemoveUserViewModel(MainModel mainModel) {

        this.mainModel = mainModel;
        usersList= FXCollections.observableArrayList();



        mainModel.addListener("AddOfflineUsers", this::addOfflineUsers);
    }







    private void addOfflineUsers(PropertyChangeEvent propertyChangeEvent) {
        ArrayList<User> use = (ArrayList<User>) propertyChangeEvent.getNewValue();

        Platform.runLater(() -> {
            System.out.println("TÖFÖFÖGK");
            use.removeIf(user -> user.getSalt().equals(" - deleted") );
            usersList.addAll(use);
            System.out.println(usersList);
        });
    }




    public ObservableList<User> getUsers() {
        return usersList;
    }
    public User getCurrentUser()
    {
        return mainModel.getCurrentUser();
    }

    public void deleteUser(User use) {

        mainModel.deleteUser(use);
        Platform.runLater(() -> {
            usersList.remove(use);
        });
    }
}
