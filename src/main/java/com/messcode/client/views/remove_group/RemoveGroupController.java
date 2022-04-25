package com.messcode.client.views.remove_group;

import com.messcode.transferobjects.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/**
 * The Controller of the RemoveGroup panel.
 * Processes the input of the user and forwards it to the ViewModel.
 * @author Kamilla Kisová
 */
public class RemoveGroupController {

    public Label errorLabel;
    public ListView<Group> groupsList;

    private RemoveGroupViewModel removeGroupVM;

    /**
     * Initialization method for the Controller. Prepares the panel and its components.
     * @param removeGroupVM ViewModel of the RemoveGroup panel
     */
    public void init(RemoveGroupViewModel removeGroupVM) {
        this.removeGroupVM = removeGroupVM;
        updateGroupList();
    }

    /**
     * Updates the list of groups from the list in the ViewModel
     */
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

    /**
     * Initiates the removal of the group
     */
    public void removeButton() {
        Group g = groupsList.getSelectionModel().getSelectedItem();
        removeGroupVM.deleteGroup(g);
    }
}
