package com.messcode.client.views.remove_group;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.util.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class RemoveGroupViewModel implements Subject {
    private MainModel mainModel;
    private PropertyChangeSupport support;
    private ObservableList<Group> groupsList;
    public RemoveGroupViewModel(MainModel mainModel) {
        this.groupsList= FXCollections.observableArrayList() ;
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
    }

    public ObservableList<Group> getGroupsList() {
        return groupsList;
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
