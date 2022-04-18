package com.messcode.client.views.new_group;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.util.ResourceBundle;

public class NewGroupController {

    private Button createButton;
    private Label errorLabel;
    private ComboBox<User> groupLeaderComboBox;
    private TextField groupNameTextField;

    private NewGroupViewModel newGroupVM;
    private ViewHandler vh;
    private User user;
    private ResourceBundle bundle;

    public void init(NewGroupViewModel newGroupVM, ViewHandler vh, ResourceBundle bundle) {
        this.newGroupVM = newGroupVM;
        this.vh = vh;
        this.bundle = bundle;
        groupLeaderComboBox.setItems(newGroupVM.getUsersList());
    }

    public void createClicked(ActionEvent event) {
        String groupName = groupNameTextField.getText();
        User groupLeader = (User) groupLeaderComboBox.getValue(); // comment for kami to edit ***


        // NOMMI do we need regex for project name?
    }
}
