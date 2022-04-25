package com.messcode.client.views.change_password;

import com.messcode.client.model.MainModel;
import com.messcode.client.core.SettingsConfig;
import com.messcode.transferobjects.util.Subject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The ViewModel of the ChangePassword panel.
 * Filters & processes the information going to and from the Controller.
 * @author Kamilla Kisová
 */
public class ChangePasswordViewModel implements Subject {

    private MainModel mainModel;
    private PropertyChangeSupport support;
    private StringProperty error;
    private ResourceBundle bundle;

    /**
     * Constructor of the ChangePasswordViewModel
     * @param mainModel the MainModel, which manages all the information in the background
     */
    public ChangePasswordViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
        error = new SimpleStringProperty();

        mainModel.addListener("passChangeResponse", this::response);
    }

    /**
     * Gets the response from the MainModel
     * @param propertyChangeEvent PropertyChangeEvent triggered event
     */
    private void response(PropertyChangeEvent propertyChangeEvent) {
        checkLanguage();
        if (((boolean) propertyChangeEvent.getNewValue())) {
            //TODO
            // write exit here.
        } else {
            error.setValue(bundle.getString("error.your_pass_is_inccorect"));
        }
    }

    /**
     * Getter for the error
     * @return StringProperty of the error
     */
    public StringProperty errorProperty() {
        return error;
    }

    /**
     * Initiates the password change
     * Checks if the new and the confirm password match
     * @param current String current password
     * @param password String new password
     * @param passwordConfirmed String new password confirmation
     */
    public void changePassword(String current, String password, String passwordConfirmed) {
        checkLanguage();
        if (passwordConfirmed.equals("") || password.equals("") || current.equals("")) {
            error.setValue(bundle.getString("error.fill_all_fields"));
        }
        if (password.equals(passwordConfirmed)) {
            mainModel.changePassword(current, password, passwordConfirmed);
        } else {
            error.setValue(bundle.getString("error.pass_do_not_match"));
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

    /**
     * Checks the language for the panel
     */
    private void checkLanguage(){
        if(SettingsConfig.getConfigOf("language").equals("SK")){
            this.bundle = ResourceBundle.getBundle("bundle", new Locale("sk", "SK"));
        } else {
            this.bundle = ResourceBundle.getBundle("bundle", new Locale("en", "EN"));
        }
    }
}
