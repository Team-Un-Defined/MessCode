package com.messcode.client.views.remove_user;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.ResourceBundle;

/**
 *
 */
public class RemoveUserController {

    public Label errorLabel;
    public ListView<User> usersList;

    private RemoveUserViewModel removeUserVM;
    private ViewHandler vh;
    private User user;
    private ResourceBundle bundle;

    /**
     * @param removeUserVM
     * @param vh
     * @param bundle
     */
    public void init(RemoveUserViewModel removeUserVM, ViewHandler vh, ResourceBundle bundle) {
        this.removeUserVM = removeUserVM;
        this.vh = vh;
        this.bundle = bundle;
        updateUserList();
    }

    /**
     *
     */
    private void updateUserList() {
        usersList.setItems(removeUserVM.getUsers());
        usersList.setCellFactory(lv -> new ListCell<User>() {
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
     *
     */
    public void removeButton() {
        if (usersList.getSelectionModel().getSelectedItems().isEmpty()) {
            errorLabel.setText(bundle.getString("select_user"));
        } else {
            User use = (User) usersList.getSelectionModel().getSelectedItems().get(0);
            System.out.println(use.getEmail());
            if (!use.getEmail().equals(removeUserVM.getCurrentUser().getEmail())) {
               removeUserVM.deleteUser(use);

            } else {
               errorLabel.setText(bundle.getString("talk_to_yourself"));
            }
        }
    }
}
