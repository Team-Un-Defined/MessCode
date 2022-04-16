package com.messcode.client.views.login;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.util.Subject;
import javafx.application.Platform;
import com.messcode.transferobjects.AccountManager;
import com.messcode.transferobjects.User;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.messcode.client.core.ViewHandler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class LoginController implements Subject {

    public TextField textField;
    public Label usernameErrorLabel;
    public PasswordField passwordField;
    private PropertyChangeSupport support;
    private LoginViewModel loginVM;
    private ViewHandler vh;

    public void init(LoginViewModel loginVM, ViewHandler vh, ResourceBundle bundle) {
        this.loginVM = loginVM;
        this.vh = vh;
        loginVM.bundle = bundle;
        support = new PropertyChangeSupport(this);
        usernameErrorLabel.textProperty().bind(loginVM.errorProperty());
        loginVM.addListener("Login", this::response);
        loginVM.addListener("OpenChat", this::openChat);
    }

    private void openChat(PropertyChangeEvent propertyChangeEvent) {
        Platform.runLater(() -> vh.openChatClientView());
    }

        /*
        if (usernameField.getText().length() >= 4) {
            String username = usernameField.getText();
            loginVM.addUser(new User(username));
            vh.openChatClientView();
        } else {
            usernameErrorLabel.setText("Username too short(min 4 char)");
            usernameErrorLabel.setText(bundle.getString("short_login"));
        }
        */

    private void response(PropertyChangeEvent propertyChangeEvent) {
        vh.openChatClientView();
    }

    public void enterChatBtn() {
        loginVM.login(textField.getText(), passwordField.getText());
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
