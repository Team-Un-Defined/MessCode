package com.messcode.client.views.new_employee;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.AccountManager;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.UserList;
import com.messcode.transferobjects.util.Subject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ResourceBundle;

public class NewEmployeeController implements Subject {

    public Button createButton;
    public TextField emailTextField;
    public Label errorLabel;
    public TextField firstNameTextField;
    public TextField lastNameTextField;
    public ComboBox typeComboBox;

    private NewEmployeeViewModel newEmployeeVM;
    private ViewHandler vh;

    private ResourceBundle bundle;
    private PropertyChangeSupport support;

    public void init(NewEmployeeViewModel newEmployeeVM, ViewHandler vh, ResourceBundle bundle) {
        this.newEmployeeVM = newEmployeeVM;
        this.vh = vh;
        this.bundle = bundle;
        ObservableList<String> types = FXCollections.observableArrayList(
                "employee",
                "employer",
                "superuser"
        );
        typeComboBox.setItems(types);
        support = new PropertyChangeSupport(this);
        errorLabel.setText("");
        errorLabel.textProperty().bind(newEmployeeVM.errorProperty());
        newEmployeeVM.addListener("accCreateResponse", this::evalute);
    }

    private void evalute(PropertyChangeEvent propertyChangeEvent) {
        System.out.println("WOTÖFÖK? AGAIN");
        errorLabel.setVisible(true);
        System.out.println("WOTÖFÖK? AGAIN2");
        // put here the exit

    }

    public void createClicked() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String type = (String) typeComboBox.getValue();
        int resp = newEmployeeVM.createAccount(firstName, lastName, email, type);
        if (resp == 1) {
            errorLabel.setVisible(true);
        } else if (resp == 2) {
            errorLabel.setVisible(false);
        } else if (resp == 3) {
            errorLabel.setVisible(true);
        } else if (resp == 0) {
            errorLabel.setVisible(true);
        }

    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }
}
