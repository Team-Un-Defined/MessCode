package com.messcode.client.views.edit_project_leader;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.util.Subject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EditProjectLeaderViewModel implements Subject {

    private PropertyChangeSupport support;
    private MainModel mainModel;

    public EditProjectLeaderViewModel(MainModel mainModel) {
        support = new PropertyChangeSupport(this);
        this.mainModel = mainModel;
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
