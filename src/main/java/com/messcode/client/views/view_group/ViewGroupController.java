package com.messcode.client.views.view_group;

import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ViewGroupController {

    public Label groupNameLabel;
    public Label projectLeaderLabel;
    public TextArea descriptionArea;
    public ListView membersList;
    public Button closeButton;

    private ViewGroupViewModel viewGroupVM;

    public void init(ViewGroupViewModel viewGroupVM) {
        this.viewGroupVM = viewGroupVM;
        groupNameLabel.setText(this.viewGroupVM.getGroupName());
        projectLeaderLabel.setText(this.viewGroupVM.getLead());
        descriptionArea.setText(this.viewGroupVM.getDescription());
        
        updateUsersInGroupList();
    }

    /**
     * Closes the ViewGroup stage
     */
    public void close() {
        Stage stage = (Stage) groupNameLabel.getScene().getWindow();
        stage.close();
    }
    
    private void updateUsersInGroupList() {
        membersList.setItems(viewGroupVM.getMembers());
        membersList.setCellFactory(lv -> new ListCell<User>() {
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
}
