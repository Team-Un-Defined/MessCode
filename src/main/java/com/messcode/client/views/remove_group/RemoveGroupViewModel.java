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
 * The ViewModel of the RemoveGroup panel.
 * Filters & processes the information going to and from the Controller.
 * @author Kamilla Kisová
 */
public class RemoveGroupViewModel implements Subject {
    private MainModel mainModel;
    private PropertyChangeSupport support;
    private ObservableList<Group> groups;

    /**
     * Constructor of the RemoveGroupViewModel
     * @param mainModel the MainModel, which manages all the information in the background
     */
    public RemoveGroupViewModel(MainModel mainModel) {
        this.groups= FXCollections.observableArrayList() ;
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
        mainModel.addListener("RefresgGroups", this::refreshGroups);
    }

    /**
     * Refreshes the list of groups
     * @param propertyChangeEvent PropertyChangeEvent triggered event
     */
    private void refreshGroups(PropertyChangeEvent propertyChangeEvent) {
        ArrayList<Group> groupList = (ArrayList<Group>) propertyChangeEvent.getNewValue();
        if(groupList==null){}else{
        Platform.runLater(() -> {
            groupList.removeIf(group -> group.getLeader() == null);
            groups.clear();
            groups.addAll(groupList);
        });}
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
     * Getter for the list of groups
     * @return ObservableList<Group>
     */
    public ObservableList<Group> getGroups() {
        return groups;
    }

    /**
     * Initiates the removal of the group
     * @param g Group
     */
    void deleteGroup(Group g) {
        mainModel.deleteGroup(g);
    }
}
