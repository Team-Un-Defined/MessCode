package com.messcode.client.views.change_password;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ResourceBundle;

public class ChangePasswordController {

    public Button confirmButton;
    public Label errorLabel;
    public TextField passwordConfirmTextField;
    public TextField passwordTextField;

    private ChangePasswordViewModel changePasswordVM;
    private ViewHandler vh;
    private User user;
    private ResourceBundle bundle;

    public void init(ChangePasswordViewModel changePasswordVM, ViewHandler vh, ResourceBundle bundle) {
        this.changePasswordVM = changePasswordVM;
        this.vh = vh;
        this.bundle = bundle;
    }

    public void confirmClicked() {
        String password = passwordTextField.getText();
        String passwordConfirmed = passwordConfirmTextField.getText();

        // NOMMI do we need regex for passwords?
    }
}
