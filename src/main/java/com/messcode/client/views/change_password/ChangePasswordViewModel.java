package com.messcode.client.views.change_password;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.util.Subject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ChangePasswordViewModel implements Subject {

    private MainModel mainModel;
    private PropertyChangeSupport support;
    private StringProperty error;

    public ChangePasswordViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
        error = new SimpleStringProperty();

        mainModel.addListener("passChangeResponse", this::response);
    }

    private void response(PropertyChangeEvent propertyChangeEvent) {
        if (((boolean) propertyChangeEvent.getNewValue())) {
            //TODO
            // write exit here.
        } else {
            error.setValue("Your current password is incorrect.");
        }
    }

    public StringProperty errorProperty() {
        return error;
    }

    public void changePassword(String current, String password, String passwordConfirmed) {
        if (passwordConfirmed.equals("") || password.equals("") || current.equals("")) {
            error.setValue("Fill in all the fields");
        }
        if (password.equals(passwordConfirmed)) {
            mainModel.changePassword(current, password, passwordConfirmed);
        } else {
            error.setValue("The new passwords do not match");
        }
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
