package com.messcode.client.views.edit_member;

import com.messcode.client.core.ViewHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.ResourceBundle;

public class EditMemberController {

    public ListView allUsersList;
    public ListView inGroupUsersList;
    public Button addButton;
    public Button removeButton;
    private EditMemberViewModel editMemberVM;
    private ViewHandler vh;
    private ResourceBundle bundle;

    public void init(EditMemberViewModel editMemberVM, ViewHandler vh, ResourceBundle bundle) {
        this.editMemberVM = editMemberVM;
        this.vh = vh;
        this.bundle = bundle;
    }

    public void addMember(ActionEvent actionEvent) {
        System.out.println("addMember fired");
    }

    public void removeMember(ActionEvent actionEvent) {
        System.out.println("removeMember fired");
    }
}
