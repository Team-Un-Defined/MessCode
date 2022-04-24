package com.messcode.client.views.view_profile;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.util.Subject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ViewProfileViewModel implements Subject {

    private MainModel mainModel;
    private PropertyChangeSupport support;

    public ViewProfileViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
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
