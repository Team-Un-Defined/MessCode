package com.messcode.client.views.view_profile;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.util.Subject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The ViewModel of the ViewProfile panel.
 * Filters & processes the information going to and from the Controller.
 * @author Kamilla Kisová
 */
public class ViewProfileViewModel implements Subject {

    private MainModel mainModel;
    private PropertyChangeSupport support;
    private User user;

    /**
     * Constructor of the ViewProfileViewModel
     * @param mainModel the MainModel, which manages all the information in the background
     */
    public ViewProfileViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
        this.user = mainModel.getSelectedUser();
    }

    /**
     * Getter for the viewed user
     * @return the viewed user
     */
    public User getUser() {
        return user;
    }

    /**
     * Setter for the viewed user
     */
    public void setUser() {
        this.user = mainModel.getSelectedUser();
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
}
