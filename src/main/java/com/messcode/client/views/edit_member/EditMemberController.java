package com.messcode.client.views.edit_member;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.util.ResourceBundle;

public class EditMemberController {

    public ListView<User> allUsersList;
    public ListView<User> inGroupUsersList;
    public Button addButton;
    public Button removeButton;
    public Label errorLabel;
    private EditMemberViewModel editMemberVM;
    private ViewHandler vh;
    private ResourceBundle bundle;

    public void init(EditMemberViewModel editMemberVM, ViewHandler vh, ResourceBundle bundle) {
        this.editMemberVM = editMemberVM;
        this.vh = vh;
        this.bundle = bundle;
        updateUserList();
        updateUsersInGroupList();
        allUsersList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        inGroupUsersList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void addMember() {
        if (allUsersList.getSelectionModel().getSelectedItems().isEmpty()) {
            errorLabel.setText(bundle.getString("select_user"));
        } else {
            ObservableList<User> u = allUsersList.getSelectionModel().getSelectedItems();
            editMemberVM.addMember(u);
        }
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

    public void removeMember() {
        if (allUsersList.getSelectionModel().getSelectedItems().isEmpty()) {
            errorLabel.setText(bundle.getString("select_user"));
        } else {
            ObservableList<User> u = inGroupUsersList.getSelectionModel().getSelectedItems();
            editMemberVM.removeMember(u);
        }
    }
}
