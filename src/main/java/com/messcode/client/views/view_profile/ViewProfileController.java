package com.messcode.client.views.view_profile;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 *
 */
public class ViewProfileController {
    public Label nameLabel;
    public Label firstNameLabel;
    public Label lastNameLabel;
    public Label emailLabel;
    public Label roleLabel;

    private ViewProfileViewModel viewProfileVM;
    private ViewHandler vh;
    private ResourceBundle bundle;

    /**
     * @param viewProfileVM
     * @param vh
     * @param bundle
     */
    public void init(ViewProfileViewModel viewProfileVM, ViewHandler vh, ResourceBundle bundle) {
        this.viewProfileVM = viewProfileVM;
        this.vh = vh;
        this.bundle = bundle;

        this.viewProfileVM.setUser();

        User user = viewProfileVM.getUser();
        nameLabel.setText(user.getName() + " " + user.getSurname());
        firstNameLabel.setText(user.getName());
        lastNameLabel.setText(user.getSurname());
        emailLabel.setText(user.getEmail());
        roleLabel.setText(user.getType());
    }

    /**
     *
     */
    public void close() {
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        stage.close();
    }
}
