package com.messcode.client.views.view_profile;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * The Controller of the ViewProfile panel.
 * Processes the input of the user and forwards it to the ViewModel.
 * @author Kamilla Kisová
 */
public class ViewProfileController {
    public Label nameLabel; // Label for the whole name (first name + last name)
    public Label firstNameLabel;
    public Label lastNameLabel;
    public Label emailLabel;
    public Label roleLabel;

    private ViewProfileViewModel viewProfileVM;

    /**
     * Initialization method for the Controller. Prepares the panel and its components.
     * @param viewProfileVM ViewModel of the ViewProfile panel
     */
    public void init(ViewProfileViewModel viewProfileVM) {
        this.viewProfileVM = viewProfileVM;

        this.viewProfileVM.setUser();

        User user = viewProfileVM.getUser();
        nameLabel.setText(user.getName() + " " + user.getSurname());
        firstNameLabel.setText(user.getName());
        lastNameLabel.setText(user.getSurname());
        emailLabel.setText(user.getEmail());
        roleLabel.setText(user.getType());
    }

    /**
     * Closes the ViewProfile stage
     */
    public void close() {
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        stage.close();
    }
}
