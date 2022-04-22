package com.messcode.client.views.change_password;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.beans.PropertyChangeSupport;
import java.util.ResourceBundle;

public class ChangePasswordController {

    public Button confirmButton;
    public Label errorLabel;
    public TextField currentPasswordTextField;
    public TextField passwordConfirmTextField;
    public TextField passwordTextField;

    private ChangePasswordViewModel changePasswordVM;
    private ViewHandler vh;

    private ResourceBundle bundle;

    public void init(ChangePasswordViewModel changePasswordVM, ViewHandler vh, ResourceBundle bundle) {
        this.changePasswordVM = changePasswordVM;
        this.vh = vh;
        this.bundle = bundle;

        errorLabel.textProperty().bind(changePasswordVM.errorProperty());
    }

    public void confirmClicked() {
        String currentPassword = currentPasswordTextField.getText();
        String password = passwordTextField.getText();
        String passwordConfirmed = passwordConfirmTextField.getText();
        changePasswordVM.changePassword(currentPassword,password,passwordConfirmed);

        // NOMMI do we need regex for passwords?
    }
}
