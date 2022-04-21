package com.messcode.client.views.remove_user;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.ResourceBundle;

public class RemoveUserController {

    public Label errorLabel;
    public ListView<User> usersList;

    private RemoveUserViewModel removeUserVM;
    private ViewHandler vh;
    private User user;
    private ResourceBundle bundle;

    public void init(RemoveUserViewModel removeUserVM, ViewHandler vh, ResourceBundle bundle) {
        this.removeUserVM = removeUserVM;
        this.vh = vh;
        this.bundle = bundle;
        usersList.setItems(removeUserVM.getUsersList());
        usersList.setCellFactory(lv -> new ListCell<>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    String text = item.getName() + " " + item.getSurname();
                    setText(text);
                }
            }
        });
    }

    public void removeButton() {
    }
}
