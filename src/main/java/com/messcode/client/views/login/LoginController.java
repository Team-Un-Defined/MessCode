package com.messcode.client.views.login;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.util.Subject;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ResourceBundle;

public class LoginController implements Subject {

    public TextField textField;
    public Label usernameErrorLabel;
    public PasswordField passwordField;
    private PropertyChangeSupport support;
    private LoginViewModel loginVM;
    private ViewHandler vh;
    private ResourceBundle bundle;

    public void init(LoginViewModel loginVM, ViewHandler vh, ResourceBundle bundle) {
        this.loginVM = loginVM;
        this.vh = vh;
        this.bundle = bundle;
        support = new PropertyChangeSupport(this);
        usernameErrorLabel.textProperty().bind(loginVM.errorProperty());
        loginVM.addListener("Login", this::response);
        loginVM.addListener("OpenChat", this::openChat);
    }

    private void openChat(PropertyChangeEvent propertyChangeEvent) {
        Platform.runLater(() -> vh.openChatClientView());
    }

    private void response(PropertyChangeEvent propertyChangeEvent) {
        vh.openChatClientView();
    }

    public void enterChatBtn() {
        loginVM.login(textField.getText(), passwordField.getText(), bundle);
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
