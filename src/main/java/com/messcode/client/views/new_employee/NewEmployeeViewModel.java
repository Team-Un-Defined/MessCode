package com.messcode.client.views.new_employee;

import com.messcode.client.core.SettingsConfig;
import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.AccountManager;
import com.messcode.transferobjects.util.Subject;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;
import java.util.Locale;
import java.util.ResourceBundle;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class NewEmployeeViewModel implements Subject {

    private MainModel mainModel;
    private PropertyChangeSupport support;
    private StringProperty error;
    private String currentEmail = "";
    private String password = "";
    private ResourceBundle bundle;

    public NewEmployeeViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
        error = new SimpleStringProperty();
        error.set("");
        mainModel.addListener("createUserResponse", this::response);
    }

    private void response(PropertyChangeEvent propertyChangeEvent) {
        checkLanguage();
        System.out.println("HELLO I SHOULD RET REPSONSE 1");
        if ((boolean) propertyChangeEvent.getNewValue()) {
            support.firePropertyChange("accCreateResponse", null, "Successfull account creation");
            Platform.runLater(() -> {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Generated password");
                ButtonType buttonType = new ButtonType("Copy password", ButtonBar.ButtonData.OK_DONE);
                dialog.setContentText("Please make sure to forward the new user their\n" +
                        "generated password: " + password);
                dialog.getDialogPane().getButtonTypes().add(buttonType);
                dialog.setOnCloseRequest(actionEvent(password));
                dialog.showAndWait();

            });
            System.out.println("HELLO I SHOULD RET REPSONSE 2 pass : " + password);
        } else {
            support.firePropertyChange("accCreateResponse", null, "not successfull");
            Platform.runLater(() -> {
                System.out.println("ThIsEMailISNalradyiNUSER");
                error.setValue(bundle.getString("new_employee.email_in_use"));
            });

            System.out.println("HELLO I SHOULD RET REPSONSE 3");
        }
    }
    private EventHandler<DialogEvent> actionEvent(String password) {
        String ctc = password;
        StringSelection stringSelection = new StringSelection(ctc);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        return null;
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
        checkLanguage();
        System.out.println("CREATE ACC 1");
        AccountManager myAccountManager = new AccountManager();
        String password;
        if (firstName.equals("") || lastName.equals("") || email.equals("") || type.equals("")) {
            error.setValue(bundle.getString("error.fields_are_empty"));
            return 0;
        }
        if (!myAccountManager.nameRegex(firstName)) {
            error.setValue(bundle.getString("error.invalid_first_name_format"));
            return 1;
        }
        if (!myAccountManager.nameRegex(lastName)) {
            error.setValue(bundle.getString("error.invalid_last_name_format"));
            return 1;
        }
        if (!myAccountManager.emailRegex(email)) {
            error.setValue(bundle.getString("error.invalid_email"));
            return 1;
        } else {
            if (!(currentEmail.equals(email))) {
                System.out.println("CREATE ACC 2");
                currentEmail = email;
                this.password = myAccountManager.generatePassword();
                error.setValue(bundle.getString("error.generated_pass") + ": " + this.password);
                mainModel.register(firstName, lastName, email, this.password, type);


                return 2;
            }
            error.setValue(bundle.getString("error.email_in_use"));

        }
        return 3;
    }

    private void checkLanguage(){
        if(SettingsConfig.getConfigOf("language").equals("SK")){
            this.bundle = ResourceBundle.getBundle("bundle", new Locale("sk", "SK"));
        } else {
            this.bundle = ResourceBundle.getBundle("bundle", new Locale("en", "EN"));
        }
    }
}
