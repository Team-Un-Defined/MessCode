package com.messcode.client.views.edit_project_leader;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.ResourceBundle;

public class EditProjectLeaderController {

    private EditProjectLeaderViewModel editProjectLeaderVM;
    private ViewHandler vh;
    private ResourceBundle bundle;
    public ComboBox<User> groupLeaderComboBox;
    public Label errorLabel;

    public void init(EditProjectLeaderViewModel editProjectLeaderVM, ViewHandler vh, ResourceBundle bundle) {
        this.editProjectLeaderVM = editProjectLeaderVM;
        this.vh = vh;
        this.bundle = bundle;
    }

    public void confirmButton() {
    }
}
