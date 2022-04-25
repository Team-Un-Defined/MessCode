package com.messcode.client.views.edit_member;

import com.messcode.transferobjects.User;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * The Controller of the EditMember panel.
 * Processes the input of the user and forwards it to the ViewModel.
 * @author Kamilla Kisová
 */
public class EditMemberController {

    public ListView<User> allUsersList;
    public ListView<User> inGroupUsersList;
    public Button addButton;
    public Button removeButton;
    public Label errorLabel;
    private EditMemberViewModel editMemberVM;
    private ResourceBundle bundle;

    /**
     * Initialization method for the Controller. Prepares the panel and its components.
     * @param editMemberVM ViewModel of the EditMember panel
     * @param bundle ResourceBundle
     */
    public void init(EditMemberViewModel editMemberVM, ResourceBundle bundle) {
        this.editMemberVM = editMemberVM;
        this.bundle = bundle;
        updateUserList();
        updateUsersInGroupList();
       
        allUsersList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        inGroupUsersList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Initiates adding the member to the group
     * Checks the selection
     */
    public void addMember() {
        if (allUsersList.getSelectionModel().getSelectedItems().isEmpty()) {
            errorLabel.setText(bundle.getString("select_user"));
        } else {
            ObservableList<User> u = allUsersList.getSelectionModel().getSelectedItems();
            editMemberVM.addMember(u);
        }
    }

    /**
     * Updates the list of all users
     */
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

    /**
     * Updates the list of users that are already in the group
     */
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

    /**
     * Initiates the removal of the member
     */
    public void removeMember() {
        if (inGroupUsersList.getSelectionModel().getSelectedItems().isEmpty()) {
            errorLabel.setText(bundle.getString("select_user"));
        } else {
            ObservableList<User> u = inGroupUsersList.getSelectionModel().getSelectedItems();
            editMemberVM.removeMember(u);
        }
    }

    /**
     * Closes the stage
     */
    public void confirmClicked() {
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }
}
