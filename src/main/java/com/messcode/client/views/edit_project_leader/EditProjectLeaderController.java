package com.messcode.client.views.edit_project_leader;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.ResourceBundle;
import javafx.scene.control.ListCell;

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
        setGroupLeaderComboBox();
    }

    public void confirmButton() {
    }


    private void setGroupLeaderComboBox(){
       groupLeaderComboBox.setItems(editProjectLeaderVM.getUsers());
       groupLeaderComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty ) {
                    setText(null);
                } else {
                    String text = item.getName() + " " + item.getSurname() + item.getSalt(); // get text from item
                    setText(text);
                }
            }

            
        });
    
    groupLeaderComboBox.getItems().add(editProjectLeaderVM.getLeader());
    groupLeaderComboBox.getSelectionModel().select(editProjectLeaderVM.getLeader());
    
    
    }
    



}
