package com.messcode.client.views.new_employee;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.AccountManager;
import com.messcode.transferobjects.util.Subject;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class NewEmployeeViewModel implements Subject {

    private MainModel mainModel;
    private PropertyChangeSupport support;
    private StringProperty error;
    private String currentEmail = "";
    private String password = "";

    public NewEmployeeViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
        error = new SimpleStringProperty();
        error.set("");
        mainModel.addListener("createUserResponse", this::response);
    }

    private void response(PropertyChangeEvent propertyChangeEvent) {
        System.out.println("HELLO I SHOULD RET REPSONSE 1");
        if ((boolean) propertyChangeEvent.getNewValue()) {
            support.firePropertyChange("accCreateResponse", null, "Successfull account creation");
            Platform.runLater(() -> {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Generated password");
                ButtonType buttonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                dialog.setContentText("Please make sure to forward the new user their\n" +
                        "generated password: " + password);
                dialog.getDialogPane().getButtonTypes().add(buttonType);
                dialog.showAndWait();
            });
            System.out.println("HELLO I SHOULD RET REPSONSE 2 pass : " + password);
        } else {
            support.firePropertyChange("accCreateResponse", null, "not successfull");
            Platform.runLater(() -> {
                error.setValue("This email is already in use");
            });

            System.out.println("HELLO I SHOULD RET REPSONSE 3");
        }
    }

    public StringProperty errorProperty() {
        return error;
    }


    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    public int createAccount(String firstName, String lastName, String email, String type) {
        System.out.println("CREATE ACC 1");
        AccountManager myAccountManager = new AccountManager();
        String password;
        if (firstName.equals("") || lastName.equals("") || email.equals("") || type.equals("")) {
            error.setValue("Some fields are empty");
            return 0;
        }
        if (!myAccountManager.emailRegex(email)) {
            error.setValue("Invalid email format!");
            return 1;
        } else {
            if (!(currentEmail.equals(email))) {
                System.out.println("CREATE ACC 2");
                currentEmail = email;
                this.password = myAccountManager.generatePassword();
                error.setValue("Generated password : " + this.password);
                mainModel.register(firstName, lastName, email, this.password, type);


                return 2;
            }
            error.setValue("This email is already in use");
        }
        return 3;
    }
}
