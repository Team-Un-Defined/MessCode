package com.messcode.client.views.login;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.util.Subject;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ResourceBundle;

/**
 * The ViewModel of the Login panel.
 * Filters & processes the information going to and from the Controller.
 * @author Kamilla Kisová
 */
public class LoginViewModel implements Subject {

    public ResourceBundle bundle;
    private MainModel mainModel;
    private PropertyChangeSupport support;
    private StringProperty error;

    /**
     * Constructor of the LoginViewModel
     * @param mainModel the MainModel, which manages all the information in the background
     */
    public LoginViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
        error = new SimpleStringProperty();
        error.set("");
        mainModel.addListener("LoginResponseToVM", this::response);
        mainModel.addListener("LoginData", this::openChatClient);
    }

    /**
     * Opens the panel ChatClient
     * @param propertyChangeEvent PropertyChangeEvent triggered event
     */
    private void openChatClient(PropertyChangeEvent propertyChangeEvent) {
        support.firePropertyChange("OpenChat", null, true);
    }

    /**
     * Gets the response for the login attempt
     * @param propertyChangeEvent PropertyChangeEvent triggered event
     */
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

    /**
     * Getter for the error
     * @return StringProperty of the error
     */
    public StringProperty errorProperty() {
        return error;
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
     * Initiates the login
     * @param email String containing email
     * @param pass String containing password
     */
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
