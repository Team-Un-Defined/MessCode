package com.messcode.client.views.change_password;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * The Controller of the ChangePassword panel.
 * Processes the input of the user and forwards it to the ViewModel.
 * @author Kamilla Kisová
 */
public class ChangePasswordController {

    public Button confirmButton;
    public Label errorLabel;
    public TextField currentPasswordTextField;
    public TextField passwordConfirmTextField;
    public TextField passwordTextField;

    private ChangePasswordViewModel changePasswordVM;

    /**
     * Initialization method for the Controller. Prepares the panel and its components.
     * @param changePasswordVM ViewModel of the ChangePassword panel
     */
    public void init(ChangePasswordViewModel changePasswordVM) {
        this.changePasswordVM = changePasswordVM;
        errorLabel.textProperty().bind(changePasswordVM.errorProperty());
    }

    /**
     * Initiates the password change
     */
    public void confirmClicked() {
        String currentPassword = currentPasswordTextField.getText();
        String password = passwordTextField.getText();
        String passwordConfirmed = passwordConfirmTextField.getText();
        changePasswordVM.changePassword(currentPassword,password,passwordConfirmed);
    }
}
