package com.messcode.client.views.chat;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import com.messcode.transferobjects.util.Subject;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class ChatClientViewModel implements Subject {

    private User currentUser;
    private PropertyChangeSupport support;
    private ObservableList<User> usersList;
    private MainModel mainModel;
    private StringProperty message;
    private StringProperty PMmessage;
    private User receiver;

    

    public ChatClientViewModel(MainModel mainModel) {
        support = new PropertyChangeSupport(this);
        message = new SimpleStringProperty();
        PMmessage = new SimpleStringProperty();
        usersList = FXCollections.observableArrayList();
        this.mainModel = mainModel;
        mainModel.addListener("AddNewUser", this::getUsersList);
        mainModel.addListener("MessageForEveryone", this::displayPublic);
        mainModel.addListener("newPM", this::displayPM);
        mainModel.addListener("SetUsernameInChat", this::setUsernameInChat);
        mainModel.addListener("RemoveUser", this::removeFromUsersList);
    }

    private void removeFromUsersList(PropertyChangeEvent propertyChangeEvent) {
        User user = (User) propertyChangeEvent.getNewValue();
        Platform.runLater(() -> {
            usersList.remove(user);
            System.out.println(usersList);
        });
    }

    private void setUsernameInChat(PropertyChangeEvent propertyChangeEvent) {
        currentUser = (User) propertyChangeEvent.getNewValue();
    }

    private void displayPublic(PropertyChangeEvent propertyChangeEvent) {
        PublicMessage publicMessage = (PublicMessage) propertyChangeEvent.getNewValue();

        message.setValue(publicMessage.getTime() + " " + publicMessage.getUsername() + ": " + publicMessage.getMsg());
        System.out.println("got to model :" + message.getValue());
    }

    private void getUsersList(PropertyChangeEvent propertyChangeEvent) {

        User user = (User) propertyChangeEvent.getNewValue();

        Platform.runLater(() -> {
            usersList.add(user);
            System.out.println(usersList);
        });
    }

    public void sendPublic(PublicMessage mess) {
        mainModel.sendPublic(mess);
    }

    public ObservableList<User> getUsersList() {
        return usersList;
    }

    public StringProperty messageProperty() {
        return message;
    }

    public StringProperty PMProperty() {
        return PMmessage;
    }

    public void sendPM(PrivateMessage mess) {
        mainModel.sendPM(mess);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void sendListOfPmRoomUsers(PrivateMessage usersPM) {
        mainModel.sendListOfPmRoomUsers(usersPM);
    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    private void displayPM(PropertyChangeEvent propertyChangeEvent) {
        
        PrivateMessage pm = (PrivateMessage) propertyChangeEvent.getNewValue();
        System.out.println("ennek kuldtek az uzit: "+pm.getReceiver().getEmail());
        System.out.println("ez kuldte az uzit: "+pm.getSender().getEmail());
        System.out.println("ezzel dumalok: " + this.receiver.getEmail());
        System.out.println("en vaok ez "+this.currentUser.getEmail());
        if(pm.getReceiver().getEmail().equals(this.receiver.getEmail()) || pm.getSender().getEmail().equals(this.receiver.getEmail())){
         PMmessage.setValue(pm.getTime() + " " + pm.getUsername() + ": " + pm.getMsg());
         System.out.println("got to PMPM :" + PMmessage.getValue());
        }    
    }
    
    public ArrayList<PublicMessage>loadPublics(){
    return mainModel.loadPublics();
    }
    public ArrayList<PrivateMessage> loadPMs(){
      return mainModel.loadPMs(currentUser,receiver);
    }
    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
    
}
