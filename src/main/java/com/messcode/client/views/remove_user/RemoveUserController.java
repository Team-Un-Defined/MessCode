package com.messcode.client.views.remove_user;

import com.messcode.transferobjects.User;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.ResourceBundle;

/**
 * The Controller of the RemoveUser panel.
 * Processes the input of the user and forwards it to the ViewModel.
 * @author Kamilla Kisová
 */
public class RemoveUserController {

    public Label errorLabel;
    public ListView<User> usersList;

    private RemoveUserViewModel removeUserVM;
    private ResourceBundle bundle;

    /**
     * Initialization method for the Controller. Prepares the panel and its components.
     * @param removeUserVM ViewModel of the RemoveUser panel
     * @param bundle ResourceBundle
     */
    public void init(RemoveUserViewModel removeUserVM, ResourceBundle bundle) {
        this.removeUserVM = removeUserVM;
        this.bundle = bundle;
        updateUserList();
    }

    /**
     * Updates the list of users from the list in the ViewModel
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
     * Initiates the removal of the user
     * Checks the selected user
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
