package com.messcode.client.views.new_group;

import com.messcode.transferobjects.AccountManager;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import javafx.scene.control.*;

import java.util.ResourceBundle;

/**
 * The Controller of the NewGroup panel.
 * Processes the input of the user and forwards it to the ViewModel.
 * @author Kamilla Kisová
 */
public class NewGroupController {

    public Button createButton;
    public Label errorLabel;
    public Label descriptionLabel;
    public TextArea descriptionTextArea;
    public ComboBox<User> groupLeaderComboBox;
    public TextField groupNameTextField;

    private NewGroupViewModel newGroupVM;
    private ResourceBundle bundle;

    /**
     * Initialization method for the Controller. Prepares the panel and its components
     * @param newGroupVM ViewModel of the NewGroup panel
     * @param bundle ResourceBundle
     */
    public void init(NewGroupViewModel newGroupVM, ResourceBundle bundle) {
        this.newGroupVM = newGroupVM;
        this.bundle = bundle;
        this.descriptionTextArea.setText(bundle.getString("new_group.write_smth_nice"));
        groupLeaderComboBox.setItems(newGroupVM.getUsersList());
        groupLeaderComboBox.setCellFactory(lv -> new ListCell<User>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty ) {
                    setText(null);
                } else {
                    String text = item.getName() + " " + item.getSurname() + item.getSalt();
                    setText(text);
                }
            }
        });
    }

    /**
     * Initializes the creation of the new group.
     * Checks the selected group name
     */
    public void createClicked() {
        String groupName = groupNameTextField.getText();
        User groupLeader = groupLeaderComboBox.getValue();
        System.out.println("  77777777777777777777777777777 " + groupLeader.getEmail());
        String description = descriptionTextArea.getText();

        AccountManager myAccountManager = new AccountManager();
        if (myAccountManager.groupNameRegex(groupName)) {
            errorLabel.setVisible(false);
            boolean what = newGroupVM.newGroup(new Group(groupName, description, groupLeader));
            
            if(!what){
            errorLabel.setText(bundle.getString("new_group.incorrect_name"));
            errorLabel.setVisible(true);
            }
        } else {
            errorLabel.setText(bundle.getString("new_group.incorrect_format"));
            errorLabel.setVisible(true);
        }
    }
}
