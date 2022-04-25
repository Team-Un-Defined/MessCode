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

/**
 *
 */
public class RemoveGroupViewModel implements Subject {
    private MainModel mainModel;
    private PropertyChangeSupport support;
    private ObservableList<Group> groups;

    /**
     * @param mainModel
     */
    public RemoveGroupViewModel(MainModel mainModel) {
        this.groups= FXCollections.observableArrayList() ;
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
        mainModel.addListener("RefresgGroups", this::refreshGroups);
    }

    /**
     * @param propertyChangeEvent
     */
    private void refreshGroups(PropertyChangeEvent propertyChangeEvent) {
        ArrayList<Group> groupList = (ArrayList<Group>) propertyChangeEvent.getNewValue();
        Platform.runLater(() -> {
            groupList.removeIf(group -> group.getLeader() == null);
            groups.clear();
            groups.addAll(groupList);
        });
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
    public ObservableList<Group> getGroups() {
        return groups;
    }

    /**
     * @param g
     */
    void deleteGroup(Group g) {
        mainModel.deleteGroup(g);
    }
}
