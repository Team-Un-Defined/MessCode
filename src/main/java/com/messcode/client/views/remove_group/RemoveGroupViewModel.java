package com.messcode.client.views.remove_group;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.util.Subject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class RemoveGroupViewModel implements Subject {
    private MainModel mainModel;
    private PropertyChangeSupport support;
    private ObservableList<Group> groups;
    
    public RemoveGroupViewModel(MainModel mainModel) {
        this.groups= FXCollections.observableArrayList() ;
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
        mainModel.addListener("RefresgGroups", this::refreshGroups);
    }

    private void refreshGroups(PropertyChangeEvent propertyChangeEvent) {
        Platform.runLater(() -> {
            groups.clear();
            groups.addAll((ArrayList<Group>) propertyChangeEvent.getNewValue());
        });
    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    public ObservableList<Group> getGroups() {
        return groups;
    }

    void deleteGroup(Group g) {
        mainModel.deleteGroup(g);
    }
}
