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

public class ChangePasswordViewModel implements Subject {

    private MainModel mainModel;
    private PropertyChangeSupport support;
    private StringProperty error;
    private ResourceBundle bundle;


    public ChangePasswordViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
        support = new PropertyChangeSupport(this);
        error = new SimpleStringProperty();

        mainModel.addListener("passChangeResponse", this::response);
    }

    private void response(PropertyChangeEvent propertyChangeEvent) {
        checkLanguage();
        if (((boolean) propertyChangeEvent.getNewValue())) {
            //TODO
            // write exit here.
        } else {
            error.setValue(bundle.getString("error.your_pass_is_inccorect"));
        }
    }

    public StringProperty errorProperty() {
        return error;
    }

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

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    private void checkLanguage(){
        if(SettingsConfig.getConfigOf("language").equals("SK")){
            this.bundle = ResourceBundle.getBundle("bundle", new Locale("sk", "SK"));
        } else {
            this.bundle = ResourceBundle.getBundle("bundle", new Locale("en", "EN"));
        }
    }
}
