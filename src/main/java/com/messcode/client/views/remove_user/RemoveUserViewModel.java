package com.messcode.client.views.remove_user;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.util.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class RemoveUserViewModel implements Subject {

    private MainModel mainModel;
    private PropertyChangeSupport support;
    private ObservableList<User> usersList;

    public RemoveUserViewModel(MainModel mainModel) {
        this.usersList= FXCollections.observableArrayList() ;
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
    }

    public ObservableList<User> getUsersList() {
        return usersList;
    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }
}
