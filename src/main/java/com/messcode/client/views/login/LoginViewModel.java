package com.messcode.client.views.login;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.util.Subject;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ResourceBundle;

public class LoginViewModel implements Subject {

    public ResourceBundle bundle;
    private MainModel mainModel;
    private PropertyChangeSupport support;
    private StringProperty error;

    public LoginViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
        error = new SimpleStringProperty();
        error.set("");
        mainModel.addListener("LoginResponseToVM", this::response);
        mainModel.addListener("LoginData", this::openChatClient);
    }

    private void openChatClient(PropertyChangeEvent propertyChangeEvent) {
        support.firePropertyChange("OpenChat", null, true);
    }

    private void response(PropertyChangeEvent propertyChangeEvent) {
        boolean answer = (boolean) propertyChangeEvent.getNewValue();
        String resp;
        System.out.println("in vm: " + answer);
        if (!answer) {
            resp = bundle.getString("login.mail_or_pass_incorrect");
        } else {
            resp = bundle.getString("login.logging_in");
            support.firePropertyChange("Login", null, answer);
        }
        Platform.runLater(() -> error.setValue(resp));
    }

    public StringProperty errorProperty() {
        return error;
    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    public void login(String email, String pass) {
        if (email.length() >= 4) {
            mainModel.addUser(email, pass);
        } else {
            Platform.runLater(() -> {
                error.setValue(bundle.getString("login.short_login"));
            });
        }
    }
}
