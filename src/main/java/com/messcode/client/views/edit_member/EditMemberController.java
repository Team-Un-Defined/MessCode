package com.messcode.client.views.edit_member;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.ResourceBundle;

public class EditMemberController {

    public ListView allUsersList;
    public ListView inGroupUsersList;
    public Button addButton;
    public Button removeButton;
    private EditMemberViewModel editMemberVM;
    private ViewHandler vh;
    private ResourceBundle bundle;

    public void init(EditMemberViewModel editMemberVM, ViewHandler vh, ResourceBundle bundle) {
        this.editMemberVM = editMemberVM;
        this.vh = vh;
        this.bundle = bundle;
        updateUserList();
        updateUsersInGroupList();
    }

    public void addMember(ActionEvent actionEvent) {
        
        
        
        
    }
    private void updateUserList() {
        allUsersList.setItems(editMemberVM.getUsers());
        allUsersList.setCellFactory(lv -> new ListCell<User>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {

                    String text = item.getName() + " " + item.getSurname(); // get text from item
                    setText(text);
                }
            }
        });
    }

    private void updateUsersInGroupList() {
        inGroupUsersList.setItems(editMemberVM.getMembers());
        inGroupUsersList.setCellFactory(lv -> new ListCell<User>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {

                    String text = item.getName() + " " + item.getSurname(); // get text from item
                    setText(text);
                }
            }
        });
    }
    public void removeMember(ActionEvent actionEvent) {
        System.out.println("removeMember fired");
    }
}
