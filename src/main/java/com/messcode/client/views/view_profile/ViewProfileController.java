package com.messcode.client.views.view_profile;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;
import javafx.scene.control.Label;

import java.util.ResourceBundle;

public class ViewProfileController {
    public Label nameLabel;
    public Label firstNameLabel;
    public Label lastNameLabel;
    public Label emailLabel;
    public Label roleLabel;

    private ViewProfileViewModel viewProfileVM;
    private ViewHandler vh;
    private User user;
    private ResourceBundle bundle;

    public void init(ViewProfileViewModel removeUserVM, ViewHandler vh, ResourceBundle bundle) {
        this.viewProfileVM = removeUserVM;
        this.vh = vh;
        this.bundle = bundle;
    }
}
