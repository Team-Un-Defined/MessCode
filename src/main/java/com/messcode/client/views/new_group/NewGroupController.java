package com.messcode.client.views.new_group;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.util.ResourceBundle;

public class NewGroupController {

    public Button createButton;
    public Label errorLabel;
    public Label descriptionLabel;
    public TextArea descriptionTextArea;
    public ComboBox<User> groupLeaderComboBox;
    public TextField groupNameTextField;

    private NewGroupViewModel newGroupVM;
    private ViewHandler vh;
    private User user;
    private ResourceBundle bundle;

    public void init(NewGroupViewModel newGroupVM, ViewHandler vh, ResourceBundle bundle) {
        this.vh = vh;
        this.newGroupVM = newGroupVM;
        this.bundle = bundle; 
        this.descriptionTextArea.setText("Write something nice");
        groupLeaderComboBox.setItems(newGroupVM.getUsersList());
        groupLeaderComboBox.setCellFactory(lv -> new ListCell<User>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {

                    String text = item.getName() + " " + item.getSurname() + item.getSalt(); // get text from item
                    setText(text);
                }
            }
        });
    }

    public void createClicked(ActionEvent event) {
        String groupName = groupNameTextField.getText();
        User groupLeader = (User) groupLeaderComboBox.getValue(); // comment for kami to edit ***


        // NOMMI do we need regex for project name?
    }
}
