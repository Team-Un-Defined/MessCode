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

/**
 * The ViewModel of the NewEmployee panel.
 * Filters & processes the information going to and from the Controller.
 * @author Kamilla Kisová
 */
public class NewEmployeeViewModel implements Subject {

    private MainModel mainModel;
    private PropertyChangeSupport support;
    private StringProperty error;
    private String currentEmail = "";
    private String password = "";
    private ResourceBundle bundle;

    /**
     * Constructor of the NewEmployeeViewModel
     * @param mainModel the MainModel, which manages all the information in the background
     */
    public NewEmployeeViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
        error = new SimpleStringProperty();
        error.set("");
        mainModel.addListener("createUserResponse", this::response);
    }

    /**
     * Receives the repsonse of the new employee creation
     * @param propertyChangeEvent PropertyChangeEvent triggered event
     */
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
                dialog.setOnCloseRequest(passwordDialogClose(password));
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

    /**
     * Copies the password upon the dialog close
     * @param password String of the password
     * @return EventHandler<DialogEvent>
     */
    private EventHandler<DialogEvent> passwordDialogClose(String password) {
        String ctc = password;
        StringSelection stringSelection = new StringSelection(ctc);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        return null;
    }

    /**
     * Getter for the error
     * @return StringProperty of the error
     */
    public StringProperty errorProperty() {
        return error;
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

    /**
     * Initiates the registering of the user
     * Checks the names and emails
     * @param firstName String first name
     * @param lastName String last name
     * @param email String email
     * @param type String type/role of user
     * @return int state
     */
    public int createAccount(String firstName, String lastName, String email, String type) {
        checkLanguage();
        System.out.println("CREATE ACC 1");
        AccountManager myAccountManager = new AccountManager();
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

    /**
     * Checks the language of the panel
     */
    private void checkLanguage(){
        if(SettingsConfig.getConfigOf("language").equals("SK")){
            this.bundle = ResourceBundle.getBundle("bundle", new Locale("sk", "SK"));
        } else {
            this.bundle = ResourceBundle.getBundle("bundle", new Locale("en", "EN"));
        }
    }
}
