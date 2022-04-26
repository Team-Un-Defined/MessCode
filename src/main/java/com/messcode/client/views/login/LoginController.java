package com.messcode.client.views.login;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.util.Subject;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ResourceBundle;

/**
 * The Controller of the Login panel.
 * Processes the input of the user and forwards it to the ViewModel.
 * @author Kamilla Kisová
 */
public class LoginController implements Subject {

    public TextField emailField;
    public Label usernameErrorLabel;
    public PasswordField passwordField;
    public Button signInButton;

    private PropertyChangeSupport support;
    private LoginViewModel loginVM;
    private ViewHandler vh;

    /**
     * Initialization method for the Controller. Prepares the panel and its components.
     * @param loginVM ViewModel of the Login panel
     * @param vh ViewHandler
     * @param bundle ResourceBundle
     */
    public void init(LoginViewModel loginVM, ViewHandler vh, ResourceBundle bundle) {
        this.loginVM = loginVM;
        this.vh = vh;
        loginVM.bundle = bundle;
        support = new PropertyChangeSupport(this);
        usernameErrorLabel.textProperty().bind(loginVM.errorProperty());
        loginVM.addListener("Login", this::response);
        loginVM.addListener("OpenChat", this::openChat);
        signInButton.setDefaultButton(true);
    }

    /**
     * Initiates the opening of the panel ChatClient
     * @param propertyChangeEvent PropertyChangeEvent triggered event
     */
    private void openChat(PropertyChangeEvent propertyChangeEvent) {
        Platform.runLater(() -> vh.openChatClientView());
    }

    /**
     * Receives the response of the attempted login
     * @param propertyChangeEvent PropertyChangeEvent triggered event
     */
    private void response(PropertyChangeEvent propertyChangeEvent) {
        vh.openChatClientView();
    }

    /**
     * Initiates the login
     */
    public void enterChatBtn() {
        loginVM.login(emailField.getText(), passwordField.getText());
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
