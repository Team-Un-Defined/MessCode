package com.messcode.client.views.new_employee;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.util.ResourceBundle;

public class NewEmployeeController {

    public Button createButton;
    public TextField emailTextField;
    public Label errorLabel;
    public TextField firstNameTextField;
    public TextField lastNameTextField;

    private NewEmployeeViewModel newEmployeeVM;
    private ViewHandler vh;
    private User user;
    private ResourceBundle bundle;

    public void init(NewEmployeeViewModel newEmployeeVM, ViewHandler vh, ResourceBundle bundle) {
        this.newEmployeeVM = newEmployeeVM;
        this.vh = vh;
        this.bundle = bundle;
    }

    public void createClicked(ActionEvent event) {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();

        // NOMMI regex for employee name & email
    }
}
