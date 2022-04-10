package com.messcode.client.views.login;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;

import java.util.ResourceBundle;

public class LoginController {
    public TextField textField;
    public Label usernameErrorLabel;
    public PasswordField passwordField;

    private LoginViewModel loginVM;
    private ViewHandler vh;
    private ResourceBundle bundle;

    public void init(LoginViewModel loginVM, ViewHandler vh, ResourceBundle bundle) {
        this.loginVM = loginVM;
        this.vh = vh;
        this.bundle = bundle;
    }

    public void enterChatBtn() {
        if (textField.getText().length() >= 4) {
            String username = textField.getText();
            loginVM.addUser(new User(username));
            vh.openChatClientView();
        } else {
            usernameErrorLabel.setText("Username too short(min 4 char)");
            usernameErrorLabel.setText(bundle.getString("short_login"));
        }
    }
}
