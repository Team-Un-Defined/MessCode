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
            resp = "Your email or password is incorrect";
        } else {
            resp = "Logging in...";
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

    public void login(String email, String pass, ResourceBundle bundle) {
        if (email.length() >= 4) {
            mainModel.addUser(email, pass);
        } else {
            Platform.runLater(() -> {
                error.setValue("Username too short(min 4 char)");
                error.setValue(bundle.getString("short_login"));
            });
        }
    }
}
