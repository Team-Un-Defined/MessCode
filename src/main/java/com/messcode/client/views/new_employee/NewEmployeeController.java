package com.messcode.client.views.new_employee;

import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.AccountManager;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.UserList;
import com.messcode.transferobjects.util.Subject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ResourceBundle;

/**
 * The Controller of the NewEmployee panel.
 * Processes the input of the user and forwards it to the ViewModel.
 * @author Kamilla Kisová
 */
public class NewEmployeeController implements Subject {

    public Button createButton;
    public TextField emailTextField;
    public Label errorLabel;
    public TextField firstNameTextField;
    public TextField lastNameTextField;
    public ComboBox<String> typeComboBox;

    private NewEmployeeViewModel newEmployeeVM;
    private PropertyChangeSupport support;

    /**
     * Initialization method for the Controller. Prepares the panel and its components.
     * @param newEmployeeVM ViewModel of the NewEmployee panel
     */
    public void init(NewEmployeeViewModel newEmployeeVM) {
        this.newEmployeeVM = newEmployeeVM;
        ObservableList<String> types = FXCollections.observableArrayList(
                "employee",
                "employer",
                "superuser" // TODO
        );
        typeComboBox.setItems(types);
        support = new PropertyChangeSupport(this);
        errorLabel.setText("");
        errorLabel.textProperty().bind(newEmployeeVM.errorProperty());
        newEmployeeVM.addListener("accCreateResponse", this::evalute);
    }

    /**
     * Receives the response of the evaluation
     * @param propertyChangeEvent PropertyChangeEvent triggered event
     */
    private void evalute(PropertyChangeEvent propertyChangeEvent) {
       
        errorLabel.setVisible(true);
       
        // put here the exit
    }

    /**
     * Initiates the new employee creation
     */
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
            Stage stage = (Stage) errorLabel.getScene().getWindow();
            stage.close();
        } else if (resp == 3) {
            errorLabel.setVisible(true);
        } else if (resp == 0) {
            errorLabel.setVisible(true);
        }
    }

    /**
     * Method for adding a listener. Inherited from Subject
     * @param eventName String name of the event
     * @param listener PropertyChangeListener listener of the event
     */
    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    /**
     * Method for removing a listener. Inherited from Subject
     * @param eventName String name of the event
     * @param listener PropertyChangeListener listener of the event
     */
    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }
}
