package com.messcode.client.views.remove_group;

import com.messcode.client.core.ViewHandler;
import com.messcode.client.views.new_group.NewGroupViewModel;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.ResourceBundle;

public class RemoveGroupController {

    public Label errorLabel;
    public ListView<Group> groupsList;

    private RemoveGroupViewModel removeGroupVM;
    private ViewHandler vh;
    private ResourceBundle bundle;

    public void init(RemoveGroupViewModel removeGroupVM, ViewHandler vh, ResourceBundle bundle) {
        this.removeGroupVM = removeGroupVM;
        this.vh = vh;
        this.bundle = bundle;
        updateGroupList();
    }

    private void updateGroupList() {
        groupsList.setItems(removeGroupVM.getGroups());
        groupsList.setCellFactory(lv -> new ListCell<>() {
            @Override
            public void updateItem(Group item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    String text = item.getName(); // get text from item
                    setText(text);
                }
            }
        });
    }

    public void removeButton() {
        Group g = groupsList.getSelectionModel().getSelectedItem();
        removeGroupVM.deleteGroup(g);
    }
}
