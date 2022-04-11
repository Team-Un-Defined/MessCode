package com.messcode.client.views.login;

import com.messcode.transferobjects.AccountManager;
import com.messcode.transferobjects.User;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.messcode.client.core.ViewHandler;

import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class LoginController {
    public TextField emailField;
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

    public void loginBtn() throws NoSuchAlgorithmException {
        AccountManager myAccountManager = new AccountManager();

        String email = emailField.getText();
        String password = passwordField.getText();

        User myUser = myAccountManager.login(email, password, vh.getUserList());

        if (myUser != null) {
            loginVM.addUser(myUser);
            vh.openChatClientView();
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
    }
}
