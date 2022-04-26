package com.messcode.client.views.edit_project_leader;

import com.messcode.transferobjects.User;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

/**
 * The Controller of the EditProjectLeader panel.
 * Processes the input of the user and forwards it to the ViewModel.
 * @author Kamilla Kisová
 */
public class EditProjectLeaderController {

    public ComboBox<User> groupLeaderComboBox;
    public Label errorLabel;
    private EditProjectLeaderViewModel editProjectLeaderVM;

    /**
     * Initialization method for the Controller. Prepares the panel and its components.
     * @param editProjectLeaderVM ViewModel of the EditProjectLeader panel
     */
    public void init(EditProjectLeaderViewModel editProjectLeaderVM) {
        this.editProjectLeaderVM = editProjectLeaderVM;
        setGroupLeaderComboBox();
    }

    /**
     * Initiates the leader change
     */
    public void confirmButton() {
        User u = groupLeaderComboBox.getSelectionModel().getSelectedItem();
        editProjectLeaderVM.changeLeader(u);
    }

    /**
     * Fills the Combobox with the possible group leaders
     */
    private void setGroupLeaderComboBox() {
        groupLeaderComboBox.setItems(editProjectLeaderVM.getUsers());
        groupLeaderComboBox.setCellFactory(lv -> new ListCell<>() {
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
        groupLeaderComboBox.getItems().add(editProjectLeaderVM.getLeader());
        groupLeaderComboBox.getSelectionModel().select(editProjectLeaderVM.getLeader());
    }
}
