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
 *
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
     * @param loginVM
     * @param vh
     * @param bundle
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
     * @param propertyChangeEvent
     */
    private void openChat(PropertyChangeEvent propertyChangeEvent) {
        Platform.runLater(() -> vh.openChatClientView());
    }

    /**
     * @param propertyChangeEvent
     */
    private void response(PropertyChangeEvent propertyChangeEvent) {
        vh.openChatClientView();
    }

    /**
     *
     */
    public void enterChatBtn() {
        loginVM.login(emailField.getText(), passwordField.getText());
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
}
