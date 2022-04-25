package com.messcode.client.views.view_group;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    }

    /**
     * Closes the ViewGroup stage
     */
    public void close() {
        Stage stage = (Stage) groupNameLabel.getScene().getWindow();
        stage.close();
    }
}
